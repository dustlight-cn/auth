package cn.dustlight.validator.annotation;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DeleteCode {
    boolean onSuccess() default true;

    int onFail() default 3;
}
