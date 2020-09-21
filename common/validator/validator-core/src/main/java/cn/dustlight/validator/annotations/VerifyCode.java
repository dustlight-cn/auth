package cn.dustlight.validator.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 核对验证码配置
 */
public @interface VerifyCode {
    /**
     * 验证码名称，用于区分不同业务的验证码。
     *
     * @return
     */
    String value() default "code";

    /**
     * 存储器配置
     *
     * @return
     */
    Store store() default @Store;

    /**
     * 验证器配置
     *
     * @return
     */
    Verifier verifier() default @Verifier;

    /**
     * 删除策略
     *
     * @return
     */
    DeleteCode delete() default @DeleteCode;

    /**
     * 参数
     *
     * @return
     */
    Parameter[] parameters() default {};
}
