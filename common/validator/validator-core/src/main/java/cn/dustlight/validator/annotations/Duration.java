package cn.dustlight.validator.annotations;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 有效时间
 */
public @interface Duration {
    long value() default 1000 * 60 * 10L;

    boolean enabled() default true;
}

