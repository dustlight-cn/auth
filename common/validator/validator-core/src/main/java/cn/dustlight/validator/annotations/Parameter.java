package cn.dustlight.validator.annotations;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 参数
 */
public @interface Parameter {
    /**
     * 参数名
     *
     * @return
     */
    String name();

    /**
     * 参数值
     *
     * @return
     */
    String value();
}
