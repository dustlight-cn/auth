package cn.dustlight.uim.services;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface GrantTypeMapper {


    @Select("SELECT grant_type FROM grant_types,client_grant_types WHERE client_grant_types.cid=#{clientId} AND grant_types.id=client_grant_types.tid")
    List<String> getClientGrantTypesByClientId(String clientId);

}
