package cn.dustlight.validator.annotation;

import cn.dustlight.validator.sender.CodeSender;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Sender {
    String value() default "defaultCodeSender";

    Class<? extends CodeSender> generatorClass() default CodeSender.class;
}
