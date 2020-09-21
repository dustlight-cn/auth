package cn.dustlight.validator.annotations;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 验证码值
 */
public @interface CodeValue {
    /**
     * 验证码名称，用于区分不同业务的验证码。
     *
     * @return
     */
    String value() default "code";
}
