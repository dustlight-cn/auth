package cn.dustlight.uim.services;

import cn.dustlight.uim.models.TemplateNode;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface TemplateManagerMapper {

    @Select("SELECT name FROM sender_templates")
    List<String> getTemplatesName();

    @Select("SELECT text FROM sender_templates where name=#{templateName}")
    String getTemplate(String templateName);

    @Insert("INSERT INTO sender_templates (name,text) VALUES (#{templateName},#{templateContent}) ON DUPLICATE KEY UPDATE text=#{templateContent}")
    boolean setTemplate(String templateName, String templateContent);

    @Select("SELECT * FROM sender_templates")
    List<TemplateNode> getTemplates();

    @Insert({"<script><foreach collection='templates' item='template' separator=';'>",
            "INSERT INTO sender_templates (name,text) VALUES (#{template.name},#{template.text}) ON DUPLICATE KEY UPDATE text=#{template.text}",
            "</foreach></script>"})
    boolean setTemplates(@Param("templates") List<TemplateNode> templates);

    @Delete({"<script>DELETE FROM sender_templates WHERE name IN " +
            "<foreach collection='templateNames' item='name' open='(' separator=',' close=')'>",
            "#{name}",
            "</foreach></script>"})
    boolean deleteTemplate(@Param("templateNames") List<String> templateNames);

    @Update("UPDATE sender_templates SET name=#{name} WHERE id=#{id}")
    boolean updateTemplateName(Integer id, String name);
}
