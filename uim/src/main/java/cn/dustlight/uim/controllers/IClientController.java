package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.IClientDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

/**
 * 开放平台业务控制器
 */
@RequestMapping("/api/client")
public interface IClientController {

    @PostMapping("/app")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult<IClientDetails> createApp(@RequestParam(name = "app_name") String appName,
                                            @RequestParam Set<String> scope,
                                            @RequestParam(name = "redirect_uri") Set<String> redirectUri,
                                            Authentication authentication);

    @GetMapping("/app/{appKey}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult<IClientDetails> getApp(@PathVariable String appKey,
                                         Authentication authentication);

    @DeleteMapping("/app/{appKey}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult deleteApp(@PathVariable String appKey,
                            Authentication authentication);

    @PostMapping("/reset_secret/{appKey}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult<String> resetAppSecret(@PathVariable String appKey,
                                         Authentication authentication);

    @PostMapping("/update_name/{appKey}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult updateAppName(@PathVariable String appKey,
                                @RequestParam(name = "app_name") String appName,
                                Authentication authentication);

    @PostMapping("/update_scope/{appKey}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult updateAppScope(@PathVariable String appKey,
                                 @RequestParam String scope,
                                 Authentication authentication);

    @PostMapping("/update_redirect_uri/{appKey}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult updateAppRedirectUri(@PathVariable String appKey,
                                       @RequestParam(name = "redirect_uri") String redirectUri,
                                       Authentication authentication);

    @GetMapping("/all_apps")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
    <T extends IClientDetails,L extends List<T>> RestfulResult<L> getAllApps();

    @GetMapping("/user_apps")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    <T extends IClientDetails,L extends List<T>> RestfulResult<L> getCurrentUserApps(Principal principal);

    @GetMapping("/user_apps/{username}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
    <T extends IClientDetails,L extends List<T>> RestfulResult<L> getUserApps(@PathVariable String username);

}
