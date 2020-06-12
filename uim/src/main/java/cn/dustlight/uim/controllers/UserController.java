package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulConstants;
import cn.dustlight.uim.RestfulResult;
import cn.dustlight.uim.configurations.UimProperties;
import cn.dustlight.uim.models.UserDetails;
import cn.dustlight.uim.services.IEmailSender;
import cn.dustlight.uim.services.IVerificationCodeGenerator;
import cn.dustlight.uim.services.UserDetailsMapper;
import cn.dustlight.uim.utils.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController implements IUserController {

    @Autowired
    private IEmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private IVerificationCodeGenerator codeGenerator;

    @Autowired
    private UimProperties uimProperties;

    @Override
    public RestfulResult sendEmailCodeRegister(String email, HttpSession session) {
        if (email == null || (email = email.trim()).length() == 0)
            return RestfulConstants.ERROR_EMAIL_INVALID;
        if (userDetailsMapper.isEmailExist(email))
            return RestfulConstants.ERROR_EMAIL_EXISTS;
        String code = codeGenerator.generatorCode(uimProperties.getRegisterVerificationCodeLength());
        session.setAttribute("code_email", code);
        session.setAttribute("email", email);
        session.setAttribute("email_verified", false);
        HashMap<String, String> data = new HashMap<>();
        data.put("code", code);
        emailSender.Send("registerVerificationCode", data, email);
        return RestfulConstants.SUCCESS;
    }

    @Override
    public RestfulResult sendEmailCodeResetPassword(String email, HttpSession session) {
        if (email == null || (email = email.trim()).length() == 0)
            return RestfulConstants.ERROR_EMAIL_INVALID;
        if (!userDetailsMapper.isEmailExist(email))
            return RestfulConstants.ERROR_USER_NOT_FOUND;
        String code = codeGenerator.generatorCode(uimProperties.getResetPasswordVerificationCodeLength());
        session.setAttribute("code_email_reset", code);
        session.setAttribute("email_reset", email);
        session.setAttribute("email_reset_verified", false);
        HashMap<String, String> data = new HashMap<>();
        data.put("code", code);
        emailSender.Send("resetPasswordVerificationCode", data, email);
        return RestfulConstants.SUCCESS;
    }

    @Override
    public RestfulResult verifyEmailRegister(String email, String code, HttpSession session) {
        if (session.getAttribute("code_email") == null || code == null)
            return RestfulConstants.ERROR_VERIFICATION_CODE_INVALID;
        if (session.getAttribute("email") == null || email == null || (email = email.trim()).length() == 0)
            return RestfulConstants.ERROR_EMAIL_INVALID;
        String code_session = session.getAttribute("code_email").toString();
        String email_session = session.getAttribute("email").toString();
        if (code_session.equals(code) && email_session.equals(email)) {
            session.setAttribute("email_verified", true);
            session.removeAttribute("code_email");
            return RestfulConstants.SUCCESS;
        }
        return RestfulConstants.ERROR_VERIFICATION_CODE_INVALID;
    }

    @Override
    public RestfulResult verifyEmailResetPassword(String email, String code, HttpSession session) {
        if (session.getAttribute("code_email_reset") == null || code == null)
            return RestfulConstants.ERROR_VERIFICATION_CODE_INVALID;
        if (session.getAttribute("email_reset") == null || email == null || (email = email.trim()).length() == 0)
            return RestfulConstants.ERROR_EMAIL_INVALID;
        String code_session = session.getAttribute("code_email_reset").toString();
        String email_session = session.getAttribute("email_reset").toString();
        if (code_session.equals(code) && email_session.equals(email)) {
            session.setAttribute("email_reset_verified", true);
            session.removeAttribute("code_email_reset");
            return RestfulConstants.SUCCESS;
        }
        return RestfulConstants.ERROR_VERIFICATION_CODE_INVALID;
    }

    @Override
    public RestfulResult register(String username, String password, String nickname, HttpSession session) {
        if (username == null || (username = username.trim()).length() < 6)
            return RestfulConstants.ERROR_USERNAME_INVALID;
        if (password == null || (password = password.trim()).length() < 6)
            return RestfulConstants.ERROR_PASSWORD_INVALID;
        if (session.getAttribute("email") == null || session.getAttribute("email_verified") == null)
            return RestfulConstants.ERROR_EMAIL_INVALID;
        if (nickname == null)
            nickname = "";
        else
            nickname = nickname.trim();
        String email = session.getAttribute("email").toString();
        boolean emailVerified = (boolean) session.getAttribute("email_verified");
        if (!emailVerified)
            return RestfulConstants.ERROR_EMAIL_INVALID;
        boolean result = userDetailsMapper.insertUser(snowflake.getNextId()
                , username
                , passwordEncoder.encode(password)
                , email
                , nickname);
        session.removeAttribute("email");
        session.removeAttribute("email_verified");
        return result ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult resetEmail(String email, Principal principal, HttpSession session) {
        if (principal == null)
            return RestfulConstants.ERROR_UNAUTHORIZED;
        if (session.getAttribute("email") == null || email == null || (email = email.trim()).length() == 0
                || session.getAttribute("email_verified") == null)
            return RestfulConstants.ERROR_EMAIL_INVALID;

        String emailSession = session.getAttribute("email").toString();
        boolean emailVerified = (boolean) session.getAttribute("email_verified");

        if (!(emailSession.equals(email) && emailVerified))
            return RestfulConstants.ERROR_EMAIL_INVALID;
        boolean result = userDetailsMapper.changeEmail(principal.getName(), email);
        session.removeAttribute("email");
        session.removeAttribute("email_verified");
        return result ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult resetPasswordByEmail(String email, String password, HttpSession session) {
        if (session.getAttribute("email_reset") == null || email == null || (email = email.trim()).length() == 0
                || session.getAttribute("email_reset_verified") == null)
            return RestfulConstants.ERROR_EMAIL_INVALID;
        String emailSession = session.getAttribute("email_reset").toString();
        boolean emailVerified = (boolean) session.getAttribute("email_reset_verified");
        if (!(emailSession.equals(email) && emailVerified))
            return RestfulConstants.ERROR_EMAIL_INVALID;
        boolean result = userDetailsMapper.changePasswordByEmail(email, passwordEncoder.encode(password));
        session.removeAttribute("email_reset");
        session.removeAttribute("email_reset_verified");
        return result ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult resetNickname(String nickname, Principal principal, HttpSession session) {
        if (principal == null)
            return RestfulConstants.ERROR_UNAUTHORIZED;
        if (nickname == null)
            nickname = "";
        boolean result = userDetailsMapper.changeNickname(principal.getName(), nickname);
        return result ? RestfulConstants.SUCCESS : RestfulConstants.ERROR_UNKNOWN;
    }

    @Override
    public RestfulResult<Boolean> isUsernameExists(String username) {
        if (username == null || (username = username.trim()).length() == 0)
            return RestfulConstants.ERROR_USERNAME_INVALID;
        return RestfulResult.success(userDetailsMapper.isUsernameExist(username));
    }

    @Override
    public RestfulResult<Boolean> isEmailExists(String email) {
        if (email == null || (email = email.trim()).length() == 0)
            return RestfulConstants.ERROR_EMAIL_INVALID;
        return RestfulResult.success(userDetailsMapper.isEmailExist(email));
    }

    @Override
    public RestfulResult<Boolean> isPhoneExists(String phone) {
        if (phone == null || (phone = phone.trim()).length() == 0)
            return RestfulConstants.ERROR_PHONE_INVALID;
        return RestfulResult.success(userDetailsMapper.isPhoneExist(phone));
    }

    @Override
    public RestfulResult<UserDetails> getUserDetails(String username, Principal principal) {
        UserDetails user = userDetailsMapper.loadUser(username);
        if(user == null)
            return RestfulConstants.ERROR_USER_NOT_FOUND;
        if(principal == null || !principal.getName().equals(username))
        {
            user.setEmail(null);
            user.setPhone(null);
            user.setRole(null);
        }
        return RestfulResult.success(user);
    }

    @Override
    public RestfulResult<List<UserDetails>> getUsersDetails(List<String> usernameArray, Principal principal) {
        return RestfulResult.success(userDetailsMapper.loadUsers(usernameArray));
    }
}