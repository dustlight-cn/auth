package cn.dustlight.validator.annotation;

import cn.dustlight.validator.verifier.CodeVerifier;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Verifier {
    String value() default "defaultCodeVerifier";

    Class<? extends CodeVerifier> generatorClass() default CodeVerifier.class;
}
