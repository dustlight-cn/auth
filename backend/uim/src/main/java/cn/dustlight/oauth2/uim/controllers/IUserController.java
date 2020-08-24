package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.RestfulResult;
import cn.dustlight.oauth2.uim.models.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RequestMapping("/api/user")
public interface IUserController {

    @PostMapping("/code/email/register")
    RestfulResult sendEmailCodeRegister(@RequestParam(required = false) String email, HttpSession session);

    @PostMapping("/code/email/resetPwd")
    RestfulResult sendEmailCodeResetPassword(@RequestParam(required = false) String email, HttpSession session);

    @PostMapping("/verify/email/register")
    RestfulResult verifyEmailRegister(@RequestParam(required = false) String email, @RequestParam(required = false) String code, HttpSession session);

    @PostMapping("/verify/email/resetPwd")
    RestfulResult verifyEmailResetPassword(@RequestParam(required = false) String email, @RequestParam(required = false) String code, HttpSession session);

    @PostMapping("/register")
    RestfulResult register(@RequestParam(required = false) String username, @RequestParam(required = false) String password, @RequestParam(required = false) String nickname, HttpSession session);

    @PostMapping("/reset/email")
    @PreAuthorize("hasAuthority('WRITE_USERINFO')")
    RestfulResult resetEmail(@RequestParam(required = false) String email, Principal principal, HttpSession session);

    @PostMapping("/reset/email/password")
    RestfulResult resetPasswordByEmail(@RequestParam(required = false) String email, @RequestParam(required = false) String password, HttpSession session);

    @PostMapping("/reset/nickname")
    @PreAuthorize("hasAuthority('WRITE_USERINFO')")
    RestfulResult resetNickname(@RequestParam(required = false) String nickname, Principal principal);

    @PostMapping("/reset/gender")
    @PreAuthorize("hasAuthority('WRITE_USERINFO')")
    RestfulResult resetGender(@RequestParam(required = false) int gender, Principal principal);

    @PostMapping("/reset/avatar")
    @PreAuthorize("hasAuthority('WRITE_USERINFO')")
    RestfulResult<String> uploadAvatar(Authentication authentication) throws IOException;

    @GetMapping("/avatar/{uid}")
    void getAvatar(@PathVariable Long uid, @RequestParam(required = false) Integer size, HttpServletResponse response, HttpServletRequest request) throws IOException;

    @GetMapping("/exists/username")
    RestfulResult<Boolean> isUsernameExists(@RequestParam(required = false) String username);

    @GetMapping("/exists/email")
    RestfulResult<Boolean> isEmailExists(@RequestParam(required = false) String email);

    @GetMapping("/exists/phone")
    RestfulResult<Boolean> isPhoneExists(@RequestParam(required = false) String phone);

    @GetMapping("/details")
    @PreAuthorize("hasAuthority('READ_USERINFO')")
    RestfulResult<UserDetails> getCurrentUserDetails(Principal principal);

    @GetMapping("/details/{username}")
    @PreAuthorize("hasAuthority('READ_USERINFO_ANY')")
    RestfulResult<UserDetails> getUserDetails(@PathVariable(required = false) String username);

    @PostMapping("/details")
    @PreAuthorize("hasAuthority('READ_USERINFO_ANY')")
    RestfulResult<List<UserDetails>> getUsersDetails(@RequestBody List<String> usernameArray);

    @PostMapping("/applyForDeveloper")
    RestfulResult applyForDeveloper(Authentication authentication);
}
