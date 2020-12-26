package cn.dustlight.auth.configurations.security;

import cn.dustlight.auth.util.UserExpressionMethods;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler() {
            @Override
            public StandardEvaluationContext createEvaluationContextInternal(Authentication authentication, MethodInvocation mi) {
                StandardEvaluationContext ec = super.createEvaluationContextInternal(authentication, mi);
                ec.setVariable("user", new UserExpressionMethods(authentication));
                return ec;
            }
        };
    }
}
