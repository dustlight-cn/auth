package cn.dustlight.oauth2.uim.client.converter;

import cn.dustlight.oauth2.uim.client.entity.IUimUser;

import java.util.Map;

public interface IUimUserConverter {

    IUimUser convert(String clientName,String clientUser, Map<String, Object> data);

}
