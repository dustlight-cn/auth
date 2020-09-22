package cn.dustlight.oauth2.uim.services;

import cn.dustlight.oauth2.uim.entities.Template;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@CacheNamespace
@Service
@Mapper
public interface TemplateManagerMapper {

    @Select("SELECT name FROM templates")
    Collection<String> getTemplatesName();

    @Select("SELECT * FROM templates where name=#{name}")
    Template getTemplate(String name);

    @Insert("INSERT INTO templates (name,uid,title,content) VALUES (#{name},#{uid},#{title},#{content}) ON DUPLICATE KEY UPDATE uid=#{uid},title=#{title},content=#{content}")
    boolean setTemplate(String name, Long uid, String title, String content);

    @Select("SELECT * FROM templates")
    Collection<Template> getTemplates();

    @Insert({"<script>INSERT INTO templates (name,uid,title,content) VALUES <foreach collection='templates' item='template' separator=';'>",
            "(#{template.name},#{template.uid},#{template.title},#{template.content})",
            "</foreach>ON DUPLICATE KEY UPDATE uid=VALUES(uid),title=VALUES(title),content=VALUES(content)</script>"})
    boolean setTemplates(@Param("templates") Collection<Template> templates);

    @Delete({"<script>DELETE FROM templates WHERE name IN " +
            "<foreach collection='names' item='name' open='(' separator=',' close=')'>",
            "#{name}",
            "</foreach></script>"})
    boolean deleteTemplate(@Param("names") Collection<String> names);
}
