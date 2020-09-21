package cn.dustlight.validator.annotations;

import org.springframework.core.annotation.AliasFor;

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
     * 参数名
     *
     * @return
     */
    @AliasFor("name")
    String value() default "";

    /**
     * 参数名
     *
     * @return
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 默认参数值
     *
     * @return
     */
    String defaultValue() default "";
}
