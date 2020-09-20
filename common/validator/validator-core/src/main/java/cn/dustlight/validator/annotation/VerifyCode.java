package cn.dustlight.validator.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface VerifyCode {
    String value() default "code";

    String store() default "defaultCodeStore";

    String verifier() default "defaultCodeVerifier";

    DeleteCode delete() default @DeleteCode;
}
