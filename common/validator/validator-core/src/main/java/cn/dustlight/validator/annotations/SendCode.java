package cn.dustlight.validator.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 验证码发送切点
 */
public @interface SendCode {
    /**
     * 验证码名称，用于区分不同业务的验证码。
     *
     * @return
     */
    String value() default "code";

    /**
     * 生成器配置
     *
     * @return
     */
    Generator generator() default @Generator;

    /**
     * 存储器配置
     *
     * @return
     */
    Store store() default @Store;

    /**
     * 发送器配置
     *
     * @return
     */
    Sender sender() default @Sender;

    /**
     * 有效时间配置
     *
     * @return
     */
    Duration duration() default @Duration;

    /**
     * 参数配置
     *
     * @return
     */
    Parameter[] parameters() default {};
}
