package cn.dustlight.oauth2.uim.configurations;

import cn.dustlight.oauth2.uim.services.AuthorityDetailsMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.*;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

public class UserApproveHandler extends ApprovalStoreUserApprovalHandler implements UserApprovalHandler {

    private AuthorityDetailsMapper mapper;

    public UserApproveHandler() {

    }

    public void setMapper(AuthorityDetailsMapper mapper) {
        this.mapper = mapper;
    }

    public AuthorityDetailsMapper getMapper() {
        return mapper;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.state(this.mapper != null, "ClientMapper must be provided");
    }

    @Override
    public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        AuthorizationRequest result = super.checkForPreApproval(authorizationRequest, userAuthentication);
        updateAuthorities(result);
        return result;
    }

    @Override
    public AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
        AuthorizationRequest result = super.updateAfterApproval(authorizationRequest, userAuthentication);
        updateAuthorities(result);
        return result;
    }

    /**
     * 从Scope获取Authorities
     *
     * @param authorizationRequest
     */
    private void updateAuthorities(AuthorizationRequest authorizationRequest) {
        Set<String> scope = null;
        if (authorizationRequest.isApproved() &&
                (scope = authorizationRequest.getScope()) != null &&
                !scope.isEmpty()) {
            String[] authorities = mapper.getAuthoritiesByScopeNames(scope.toArray(new String[scope.size()]));
            Set<GrantedAuthority> au = new HashSet<>();
            au.addAll(authorizationRequest.getAuthorities());
            au.addAll(AuthorityUtils.createAuthorityList(authorities));
            authorizationRequest.setAuthorities(au);
        }
    }
}
