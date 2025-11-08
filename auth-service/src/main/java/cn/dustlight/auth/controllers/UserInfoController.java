package cn.dustlight.auth.controllers;

import cn.dustlight.auth.entities.User;
import cn.dustlight.auth.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * OpenID Connect UserInfo Endpoint
 * Returns claims about the authenticated end-user
 */
@Tag(name = "UserInfo", description = "OpenID Connect UserInfo Endpoint")
@RestController
@RequestMapping(value = Constants.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
@CrossOrigin(origins = Constants.CrossOrigin.origin, allowCredentials = Constants.CrossOrigin.allowCredentials)
public class UserInfoController {

    @Operation(summary = "获取用户信息 (OpenID Connect UserInfo Endpoint)", 
               description = "返回已认证用户的标准 OpenID Connect 用户信息声明",
               security = @SecurityRequirement(name = "AccessToken"))
    @GetMapping("userinfo")
    public Map<String, Object> getUserInfo(OAuth2Authentication authentication) {
        if (authentication == null || authentication.getUserAuthentication() == null) {
            throw new IllegalArgumentException("User authentication is required");
        }

        Map<String, Object> userInfo = new HashMap<>();
        Authentication userAuth = authentication.getUserAuthentication();
        Object principal = userAuth.getPrincipal();
        
        if (principal instanceof User) {
            User user = (User) principal;
            Set<String> scopes = authentication.getOAuth2Request().getScope();
            
            // Standard OpenID Connect claims
            // 'sub' (subject) is always returned
            userInfo.put("sub", user.getUsername());
            
            // Profile scope claims
            if (scopes.contains("profile") || scopes.contains("openid")) {
                if (user.getNickname() != null) {
                    userInfo.put("name", user.getNickname());
                    userInfo.put("preferred_username", user.getUsername());
                }
                if (user.getAvatar() != null) {
                    userInfo.put("picture", user.getAvatar());
                }
                if (user.getGender() != 0) {
                    // Map gender integer to string: 1=male, 2=female, 0=unspecified
                    String gender = user.getGender() == 1 ? "male" : user.getGender() == 2 ? "female" : "other";
                    userInfo.put("gender", gender);
                }
                if (user.getUpdatedAt() != null) {
                    userInfo.put("updated_at", user.getUpdatedAt().getTime() / 1000);
                }
            }
            
            // Email scope claims
            if (scopes.contains("email")) {
                if (user.getEmail() != null) {
                    userInfo.put("email", user.getEmail());
                    userInfo.put("email_verified", true); // Assuming verified if present
                }
            }
            
            // Phone scope claims
            if (scopes.contains("phone")) {
                if (user.getPhone() != null) {
                    userInfo.put("phone_number", user.getPhone());
                    userInfo.put("phone_number_verified", true); // Assuming verified if present
                }
            }
        }
        
        return userInfo;
    }

    @Operation(summary = "获取用户信息 (POST)", 
               description = "通过 POST 方法返回已认证用户的标准 OpenID Connect 用户信息声明",
               security = @SecurityRequirement(name = "AccessToken"))
    @PostMapping("userinfo")
    public Map<String, Object> getUserInfoPost(OAuth2Authentication authentication) {
        return getUserInfo(authentication);
    }
}
