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
}
