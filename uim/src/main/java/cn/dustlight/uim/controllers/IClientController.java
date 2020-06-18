package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.IAppDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 开放平台业务控制器
 */
@RequestMapping("/api/client")
public interface IClientController {

    @PostMapping("/app")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult<IAppDetails> createApp(@RequestParam(name = "app_name") String appName,
                                         @RequestParam String scope,
                                         @RequestParam(name = "redirect_uri") String redirectUri,
                                         Authentication authentication);

    @GetMapping("/app/{appKey}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult<IAppDetails> getApp(@PathVariable String appKey,
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
    RestfulResult<List<IAppDetails>> getAllApps();

    @GetMapping("/user_apps")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN','DEV')")
    RestfulResult<List<IAppDetails>> getCurrentUserApps();

    @GetMapping("/user_apps/{uid}")
    @PreAuthorize("hasAnyRole('ROOT','ADMIN')")
    RestfulResult<List<IAppDetails>> getUserApps(@PathVariable Long uid);

}
