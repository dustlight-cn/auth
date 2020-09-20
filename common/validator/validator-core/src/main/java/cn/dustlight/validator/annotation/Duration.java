package cn.dustlight.validator.annotation;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Duration {
    long value() default 1000 * 60 * 10L;

    boolean enabled() default true;
}

