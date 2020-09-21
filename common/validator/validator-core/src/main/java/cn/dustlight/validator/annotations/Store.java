package cn.dustlight.validator.annotations;

import cn.dustlight.validator.store.CodeStore;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 存储器配置
 */
public @interface Store {
    /**
     * 存储器Bean名
     *
     * @return
     */
    String value() default "defaultCodeStore";

    /**
     * 存储器类型
     *
     * @return
     */
    Class<? extends CodeStore> type() default CodeStore.class;
}
