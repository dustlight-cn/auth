package cn.dustlight.oauth2.uim.mappers.v1;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;

@Mapper
public interface AuthorityMapper {

    @Select("SELECT authorityName FROM role_authority AS ra,authorities AS a " +
            "WHERE ra.rid=#{rid} AND ra.aid=a.aid")
    Collection<String> listRoleAuthorities(Long rid);
}
