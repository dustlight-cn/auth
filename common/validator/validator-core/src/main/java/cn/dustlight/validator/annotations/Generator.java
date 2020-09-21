package cn.dustlight.validator.annotations;

import cn.dustlight.validator.generator.CodeGenerator;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 生成器配置
 */
public @interface Generator {
    /**
     * 生成器Bean名
     *
     * @return
     */
    String value() default "defaultCodeGenerator";

    /**
     * 生成器类型
     *
     * @return
     */
    Class<? extends CodeGenerator> type() default CodeGenerator.class;
}
