package cn.dustlight.validator.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SendCode {
    String value() default "code";

    String generator() default "defaultCodeGenerator";

    String store() default "defaultCodeStore";

    String sender() default "defaultCodeSender";

    Duration duration() default @Duration;
}
