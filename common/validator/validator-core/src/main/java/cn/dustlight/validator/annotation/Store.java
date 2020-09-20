package cn.dustlight.validator.annotation;

import cn.dustlight.validator.store.CodeStore;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Store {
    String value() default "defaultCodeStore";

    Class<? extends CodeStore> generatorClass() default CodeStore.class;
}
