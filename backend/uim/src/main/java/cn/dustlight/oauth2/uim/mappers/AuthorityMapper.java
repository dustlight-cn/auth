package cn.dustlight.oauth2.uim.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface AuthorityMapper {

    @Select("SELECT authorityName FROM role_authority AS ra,authorities AS a " +
            "WHERE ra.rid=#{rid} AND ra.aid=a.aid")
    Collection<String> listRoleAuthorities(Long rid);

    @Select("<script>SELECT * FROM authorities as a,scope_authority as sa WHERE a.aid=sa.aid AND sa.sid IN " +
            "(SELECT sid FROM scopes WHERE name IN " +
            "<foreach collection='scopeNames' item='scopeName' separator=',' open='(' close=')'>#{scopeName}</foreach>" +
            ")</script>")
    Collection<String> listScopeAuthorities(Collection<String> scopeNames);
}
