package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.oauth2.uim.entities.UserPublicDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping(value = "/api/user", produces = "application/json;charset=utf-8")
public interface IUserController {

    @PostMapping("/code/email/register")
    void sendEmailCodeRegister(@RequestParam(required = false) String email) throws IOException;

    @PostMapping("/code/email/resetPwd")
    void sendEmailCodeResetPassword(@RequestParam(required = false) String email) throws IOException;

    @PostMapping("/verify/email/register")
    void verifyEmailRegister(@RequestParam(required = false) String email,
                             @RequestParam(required = false) String code);

    @PostMapping("/verify/email/resetPwd")
    void verifyEmailResetPassword(@RequestParam(required = false) String email,
                                  @RequestParam(required = false) String code);

    @PostMapping("/register")
    void register(@RequestParam(required = false) String username,
                  @RequestParam(required = false) String password,
                  @RequestParam(required = false) String nickname);

    @PostMapping("/reset/email")
    @PreAuthorize("hasAuthority('WRITE_USERINFO')")
    void resetEmail(@RequestParam(required = false) String email);

    @PostMapping("/reset/email/password")
    void resetPasswordByEmail(@RequestParam(required = false) String email,
                              @RequestParam(required = false) String password);

    @PostMapping("/reset/nickname")
    @PreAuthorize("hasAuthority('WRITE_USERINFO')")
    void resetNickname(@RequestParam(required = false) String nickname);

    @PostMapping("/reset/gender")
    @PreAuthorize("hasAuthority('WRITE_USERINFO')")
    void resetGender(@RequestParam(required = false) int gender);

    @PostMapping("/reset/avatar")
    @PreAuthorize("hasAuthority('WRITE_USERINFO')")
    String uploadAvatar() throws IOException;

    @GetMapping("/avatar/{uid}")
    void getAvatar(@PathVariable Long uid,
                   @RequestParam(required = false) Integer size,
                   @RequestParam(required = false) Long t) throws IOException;

    @GetMapping("/exists/username")
    Boolean isUsernameExists(@RequestParam(required = false) String username);

    @GetMapping("/exists/email")
    Boolean isEmailExists(@RequestParam(required = false) String email);

    @GetMapping("/exists/phone")
    Boolean isPhoneExists(@RequestParam(required = false) String phone);

    @GetMapping("/details")
    @PreAuthorize("hasAuthority('READ_USERINFO')")
    UimUser getCurrentUserDetails();

    @GetMapping("/details/{username}")
    @PreAuthorize("hasAuthority('READ_USERINFO_ANY')")
    UimUser getUserDetails(@PathVariable(required = false) String username);

    @PostMapping("/public_details")
    List<UserPublicDetails> getUsersDetails(@RequestBody List<String> usernameArray);

    @PostMapping("/applyForDeveloper")
    void applyForDeveloper();
}
