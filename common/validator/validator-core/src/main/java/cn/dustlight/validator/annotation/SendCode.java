package cn.dustlight.validator.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SendCode {
    String value() default "code";

    String parameter() default "code";

    Generator generator() default @Generator;

    Store store() default @Store;

    Sender sender() default @Sender;

    Duration duration() default @Duration;
}
