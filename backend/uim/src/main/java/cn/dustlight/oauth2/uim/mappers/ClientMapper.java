package cn.dustlight.oauth2.uim.mappers;

import cn.dustlight.oauth2.uim.entities.v1.clients.DefaultUimClient;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Service
@Mapper
public interface ClientMapper {

    @Select("SELECT * FROM clients WHERE cid=#{clientId}")
    @Results(id = "Client", value = {
            @Result(column = "cid", property = "cid"),
            @Result(column = "cid", property = "types", many = @Many(select = "cn.dustlight.oauth2.uim.mappers.GrantTypeMapper.getClientGrantTypes")),
            @Result(column = "cid", property = "scopes", many = @Many(select = "cn.dustlight.oauth2.uim.mappers.ScopeMapper.getClientScopes"))
    })
    DefaultUimClient loadClient(String clientId);
}
