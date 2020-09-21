package cn.dustlight.validator.annotations;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 删除策略
 */
public @interface DeleteCode {
    /**
     * 验证成功后是否删除
     *
     * @return
     */
    boolean onSuccess() default true;

    /**
     * 验证失败后删除
     *
     * @return 失败次数
     */
    int onFail() default 3;
}
