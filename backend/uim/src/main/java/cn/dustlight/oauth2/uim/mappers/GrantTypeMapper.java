package cn.dustlight.oauth2.uim.mappers;

import cn.dustlight.oauth2.uim.entities.v1.types.DefaultGrantType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Mapper
public interface GrantTypeMapper {

    @Select("SELECT types.* FROM types,client_type WHERE client_type.cid=#{cid} AND client_type.tid=types.tid")
    Collection<DefaultGrantType> getClientGrantTypes(String cid);
}
