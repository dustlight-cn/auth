package cn.dustlight.validator.annotations;

import cn.dustlight.validator.configuration.ValidatorConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ValidatorConfiguration.class})
public @interface EnableValidator {
    /**
     * false: JDK代理，true: CGLIB代理
     *
     * @return
     */
    boolean proxyTargetClass() default false;

    /**
     * 发送优先级
     *
     * @return
     */
    int orderOfSend() default 2147483647;

    /**
     * 验证优先级
     *
     * @return
     */
    int orderOfVerify() default Ordered.HIGHEST_PRECEDENCE;
}
