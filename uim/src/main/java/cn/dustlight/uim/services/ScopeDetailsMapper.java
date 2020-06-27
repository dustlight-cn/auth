package cn.dustlight.uim.services;

import cn.dustlight.uim.models.ScopeDetails;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheNamespace
@Service
@Mapper
public interface ScopeDetailsMapper {


    @Select("SELECT * FROM scope_details")
    @Results(id = "ScopeDetails", value = {
            @Result(property = "name", column = "scope_name")
    })
    List<ScopeDetails> getScopes();
}
