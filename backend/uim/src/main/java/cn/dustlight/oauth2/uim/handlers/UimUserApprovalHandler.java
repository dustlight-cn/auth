package cn.dustlight.oauth2.uim.handlers;

import cn.dustlight.oauth2.uim.mappers.AuthorityMapper;
import cn.dustlight.oauth2.uim.services.AuthorityDetailsMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UimUserApprovalHandler extends ApprovalStoreUserApprovalHandler implements UserApprovalHandler {

    private AuthorityMapper mapper;

    public UimUserApprovalHandler() {

    }

    public void setMapper(AuthorityMapper mapper) {
        this.mapper = mapper;
    }

    public AuthorityMapper getMapper() {
        return mapper;
    }

    @Override
    public void afterPropertiesSet() {
        // super.afterPropertiesSet();
        // Assert.state(this.mapper != null, "ClientMapper must be provided");
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
            Collection<String> authorities = mapper.listScopeAuthorities(scope);
            Set<GrantedAuthority> au = new HashSet<>();
            au.addAll(authorizationRequest.getAuthorities());
            au.addAll(AuthorityUtils.createAuthorityList(authorities.toArray(new String[0])));
            authorizationRequest.setAuthorities(au);
        }
    }
}
