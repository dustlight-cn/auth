package cn.dustlight.validator.annotations;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 验证码参数
 */
public @interface CodeParam {
    /**
     * 验证码名称，用于区分不同业务的验证码。
     *
     * @return
     */
    String value() default "code";

    /**
     * 参数名
     *
     * @return
     */
    String name() default "";

    /**
     * 默认参数值
     *
     * @return
     */
    String defaultValue() default "";
}
