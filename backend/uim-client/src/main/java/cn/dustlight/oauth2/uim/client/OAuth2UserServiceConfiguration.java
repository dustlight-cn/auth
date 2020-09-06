package cn.dustlight.oauth2.uim.client;

import cn.dustlight.oauth2.uim.client.converter.IUimUserConverter;
import cn.dustlight.oauth2.uim.client.services.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class OAuth2UserServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean(OAuth2UserService.class)
    public OAuth2UserService oAuth2UserService(@Autowired UimClientProperties properties) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<String, Class<? extends IUimUserConverter>> classMap = properties.getCustomConverter();
        Map<String, IUimUserConverter> instanceMap = new LinkedHashMap<>();
        if (classMap != null) {
            for (String key : classMap.keySet()) {
                Class<? extends IUimUserConverter> clazz = classMap.get(key);
                if (clazz == null)
                    continue;
                instanceMap.put(key, clazz.getDeclaredConstructor().newInstance());
            }
        }
        return new OAuth2UserService(instanceMap, properties.getUserDataPath());
    }
}
