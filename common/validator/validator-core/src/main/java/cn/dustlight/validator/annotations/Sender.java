package cn.dustlight.validator.annotations;

import cn.dustlight.validator.sender.CodeSender;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 发送器配置
 */
public @interface Sender {
    /**
     * 发送器Bean名
     *
     * @return
     */
    String value() default "defaultCodeSender";

    /**
     * 发送器类型
     *
     * @return
     */
    Class<? extends CodeSender> type() default CodeSender.class;
}
