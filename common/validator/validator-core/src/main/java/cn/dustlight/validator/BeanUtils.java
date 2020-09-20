package cn.dustlight.validator;

import org.springframework.beans.factory.BeanFactory;

public class BeanUtils {

    public static <T> T getBean(BeanFactory factory, String name, Class<? extends T> clazz) {
        return factory.getBean(name, clazz);
    }
}
