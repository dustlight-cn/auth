package cn.dustlight.validator.annotations;

import cn.dustlight.validator.verifier.CodeVerifier;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 验证其器配置
 */
public @interface Verifier {
    /**
     * 验证器Bean名
     *
     * @return
     */
    String value() default "defaultCodeVerifier";

    /**
     * 验证器类型
     *
     * @return
     */
    Class<? extends CodeVerifier> type() default CodeVerifier.class;
}
