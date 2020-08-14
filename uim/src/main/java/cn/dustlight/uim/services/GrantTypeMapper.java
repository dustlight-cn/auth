package cn.dustlight.uim.services;

import cn.dustlight.uim.models.GrantType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface GrantTypeMapper {

    @Select("SELECT id,grant_type FROM grant_types")
    @Results(id = "GrantType", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "grant_type")
    })
    List<GrantType> getGrantTypes();
}
