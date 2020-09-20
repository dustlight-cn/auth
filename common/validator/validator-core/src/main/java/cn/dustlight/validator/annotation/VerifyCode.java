package cn.dustlight.validator.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface VerifyCode {
    String value() default "code";

    String parameter() default "code";

    Store store() default @Store;

    Verifier verifier() default @Verifier;

    DeleteCode delete() default @DeleteCode;
}
