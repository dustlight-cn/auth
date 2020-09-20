package cn.dustlight.validator.annotation;

import cn.dustlight.validator.generator.CodeGenerator;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Generator {
    String value() default "defaultCodeGenerator";

    Class<? extends CodeGenerator> generatorClass() default CodeGenerator.class;
}
