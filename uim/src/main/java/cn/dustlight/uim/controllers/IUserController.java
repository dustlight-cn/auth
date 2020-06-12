package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.models.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    RestfulResult resetEmail(@RequestParam(required = false) String email, Principal principal, HttpSession session);

    @PostMapping("/reset/email/password")
    RestfulResult resetPasswordByEmail(@RequestParam(required = false) String email, @RequestParam(required = false) String password, HttpSession session);

    @PostMapping("/reset/nickname")
    RestfulResult resetNickname(@RequestParam(required = false) String nickname, Principal principal, HttpSession session);

    @GetMapping("/exists/username")
    RestfulResult<Boolean> isUsernameExists(@RequestParam(required = false) String username);

    @GetMapping("/exists/email")
    RestfulResult<Boolean> isEmailExists(@RequestParam(required = false) String email);

    @GetMapping("/exists/phone")
    RestfulResult<Boolean> isPhoneExists(@RequestParam(required = false) String phone);

    @PreAuthorize("hasAuthority('ROLE_BASE')")
    @GetMapping("/details/{username}")
    RestfulResult<UserDetails> getUserDetails(@PathVariable(required = false) String username, Principal principal);

    @PreAuthorize("hasAuthority('ROLE_BASE')")
    @PostMapping("/details")
    RestfulResult<List<UserDetails>> getUsersDetails(@RequestBody List<String> usernameArray, Principal principal);
}
