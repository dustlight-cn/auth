package cn.dustlight.oauth2.uim.services;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface ResourceDetailsMapper {

    @Select("SELECT resource_name FROM resource_details,client_resource WHERE client_resource.cid=#{clientId} AND resource_details.id=client_resource.rid")
    List<String> getClientResourceIdsByClientId(String clientId);

}
