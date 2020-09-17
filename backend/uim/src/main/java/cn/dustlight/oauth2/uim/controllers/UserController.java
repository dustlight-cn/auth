package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.generator.snowflake.SnowflakeIdGenerator;
import cn.dustlight.oauth2.uim.configurations.UimProperties;
import cn.dustlight.oauth2.uim.handlers.code.VerificationCodeGenerator;
import cn.dustlight.oauth2.uim.entities.UserPublicDetails;
import cn.dustlight.oauth2.uim.entities.errors.ErrorEnum;
import cn.dustlight.oauth2.uim.services.code.VerificationCodeStoreService;
import cn.dustlight.storage.core.Permission;
import cn.dustlight.storage.tencent.cos.TencentCloudObjectStorage;
import cn.dustlight.oauth2.uim.entities.v1.users.UimUser;
import cn.dustlight.oauth2.uim.entities.User;
import cn.dustlight.oauth2.uim.handlers.email.EmailSenderHandler;
import cn.dustlight.oauth2.uim.services.UserDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

//@RestController
public class UserController implements IUserController {

    @Autowired
    private EmailSenderHandler emailSenderHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private SnowflakeIdGenerator snowflake;

    @Autowired
    private VerificationCodeGenerator codeGenerator;

    @Autowired
    private VerificationCodeStoreService codeStoreService;

    @Autowired
    private UimProperties uimProperties;

    @Autowired
    private TencentCloudObjectStorage storage;

    @Autowired
    private UimUser userDetails;

    @Override
    public void sendEmailCodeRegister(String email) throws IOException {
        if (email == null || (email = email.trim()).length() == 0)
            ErrorEnum.EMAIL_INVALID.throwException();
        if (userDetailsMapper.isEmailExist(email))
            ErrorEnum.EMAIL_EXISTS.throwException();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        String code = codeGenerator.generateCode(uimProperties.getRegisterVerificationCodeLength());
        session.setAttribute("email", email);
        session.setAttribute("email_verified", false);
        codeStoreService.store("code_email:" + session.getId(), code, 1000 * 60 * 30);
        HashMap<String, Object> data = new HashMap<>();
        data.put("code", code);
        emailSenderHandler.send(uimProperties.getRegisterEmail(), data, email);
    }

    @Override
    public void sendEmailCodeResetPassword(String email) throws IOException {
        if (email == null || (email = email.trim()).length() == 0)
            ErrorEnum.EMAIL_INVALID.throwException();
        if (!userDetailsMapper.isEmailExist(email))
            ErrorEnum.USER_NOT_FOUND.throwException();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        String code = codeGenerator.generateCode(uimProperties.getResetPasswordVerificationCodeLength());
        session.setAttribute("email_reset", email);
        session.setAttribute("email_reset_verified", false);
        codeStoreService.store("code_email_reset:" + session.getId(), code, 1000 * 60 * 30);
        HashMap<String, Object> data = new HashMap<>();
        data.put("code", code);
        emailSenderHandler.send(uimProperties.getResetPasswordEmail(), data, email);
    }

    @Override
    public void verifyEmailRegister(String email, String code) {
        if (code == null)
            ErrorEnum.CODE_INVALID.throwException();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        if (session.getAttribute("email") == null || email == null || (email = email.trim()).length() == 0)
            ErrorEnum.EMAIL_INVALID.throwException();
        String codeKey = "code_email:" + session.getId();
        String email_session = session.getAttribute("email").toString();
        if (!email_session.equals(email))
            ErrorEnum.CODE_INVALID.throwException();
        if (!codeStoreService.verify(codeKey, code))
            ErrorEnum.CODE_INVALID.throwException();
        session.setAttribute("email_verified", true);
        codeStoreService.remove(codeKey);
    }

    @Override
    public void verifyEmailResetPassword(String email, String code) {
        if (code == null)
            ErrorEnum.CODE_INVALID.throwException();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        if (session.getAttribute("email_reset") == null || email == null || (email = email.trim()).length() == 0)
            ErrorEnum.EMAIL_INVALID.throwException();
        String codeKey = "code_email_reset:" + session.getId();
        String email_session = session.getAttribute("email_reset").toString();
        if (!email_session.equals(email))
            ErrorEnum.CODE_INVALID.throwException();
        if (!codeStoreService.verify(codeKey, code))
            ErrorEnum.CODE_INVALID.throwException();
        session.setAttribute("email_reset_verified", true);
        codeStoreService.remove(codeKey);
    }

    @Override
    public void register(String username, String password, String nickname) {
        if (username == null || (username = username.trim()).length() < 6)
            ErrorEnum.USERNAME_INVALID.throwException();
        if (password == null || (password = password.trim()).length() < 6)
            ErrorEnum.PASSWORD_INVALID.throwException();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        if (session.getAttribute("email") == null || session.getAttribute("email_verified") == null)
            ErrorEnum.EMAIL_INVALID.throwException();
        if (nickname == null)
            nickname = "";
        else
            nickname = nickname.trim();
        String email = session.getAttribute("email").toString();
        boolean emailVerified = (boolean) session.getAttribute("email_verified");
        if (!emailVerified)
            ErrorEnum.EMAIL_INVALID.throwException();
        boolean result = userDetailsMapper.insertUser(snowflake.generate()
                , username
                , passwordEncoder.encode(password)
                , email
                , nickname);
        session.removeAttribute("email");
        session.removeAttribute("email_verified");
        if (!result)
            ErrorEnum.REGISTER_FAIL.throwException();
    }

    @Override
    public void resetEmail(String email) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        if (session.getAttribute("email") == null || email == null || (email = email.trim()).length() == 0
                || session.getAttribute("email_verified") == null)
            ErrorEnum.EMAIL_INVALID.throwException();

        String emailSession = session.getAttribute("email").toString();
        boolean emailVerified = (boolean) session.getAttribute("email_verified");

        if (!(emailSession.equals(email) && emailVerified))
            ErrorEnum.EMAIL_INVALID.throwException();
        boolean result = userDetailsMapper.changeEmail(userDetails.getUid(), email);
        session.removeAttribute("email");
        session.removeAttribute("email_verified");
    }

    @Override
    public void resetPasswordByEmail(String email, String password) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        if (session.getAttribute("email_reset") == null || email == null || (email = email.trim()).length() == 0
                || session.getAttribute("email_reset_verified") == null)
            ErrorEnum.EMAIL_INVALID.throwException();
        String emailSession = session.getAttribute("email_reset").toString();
        boolean emailVerified = (boolean) session.getAttribute("email_reset_verified");
        if (!(emailSession.equals(email) && emailVerified))
            ErrorEnum.EMAIL_INVALID.throwException();
        boolean result = userDetailsMapper.changePasswordByEmail(email, passwordEncoder.encode(password));
        session.removeAttribute("email_reset");
        session.removeAttribute("email_reset_verified");
    }

    @Override
    public void resetNickname(String nickname) {
        if (nickname == null)
            nickname = "";
        boolean result = userDetailsMapper.changeNickname(userDetails.getUid(), nickname);
    }

    @Override
    public void resetGender(int gender) {
        boolean result = userDetailsMapper.changeGender(userDetails.getUid(), gender);
    }

    @Override
    public String uploadAvatar() throws IOException {
        String url = storage.generatePutUrl(uimProperties.getStorage().getStoragePath() + "avatar/" + userDetails.getUid(),
                Permission.READABLE,
                uimProperties.getStorage().getDefaultExpiration());
        return url;
    }

    @Override
    public void getAvatar(Long uid, Integer size, Long t) throws IOException {
        String url = generateAvatarUrl(uid, size, t);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        if (url == null)
            response.sendError(404);
        else
            response.sendRedirect(url);
    }

    @Override
    public Boolean isUsernameExists(String username) {
        if (username == null || (username = username.trim()).length() == 0)
            ErrorEnum.USERNAME_INVALID.throwException();
        return userDetailsMapper.isUsernameExist(username);
    }

    @Override
    public Boolean isEmailExists(String email) {
        if (email == null || (email = email.trim()).length() == 0)
            ErrorEnum.EMAIL_INVALID.throwException();
        return userDetailsMapper.isEmailExist(email);
    }

    @Override
    public Boolean isPhoneExists(String phone) {
        if (phone == null || (phone = phone.trim()).length() == 0)
            ErrorEnum.PHONE_INVALID.throwException();
        return userDetailsMapper.isPhoneExist(phone);
    }

    @Override
    public UimUser getCurrentUserDetails() {
        User user = userDetailsMapper.loadUser(userDetails.getUsername());
        if (user == null)
            ErrorEnum.USER_NOT_FOUND.throwException();
        user.setAvatar(generateAvatarUrl(user.getUid(), 256, null));
        return user;
    }

    @Override
    public UimUser getUserDetails(String username) {
        User user = userDetailsMapper.loadUser(username);
        if (user == null)
            ErrorEnum.USER_NOT_FOUND.throwException();
        user.setAvatar(generateAvatarUrl(user.getUid(), 256, null));
        return user;
    }

    @Override
    public List<UserPublicDetails> getUsersDetails(List<String> usernameArray) {
        List<UserPublicDetails> arr = userDetailsMapper.loadUsersPublic(usernameArray);
        for (UserPublicDetails userPublicDetails : arr)
            userPublicDetails.setAvatar(generateAvatarUrl(userPublicDetails.getUid(), 256, null));
        return arr;
    }

    @Override
    public void applyForDeveloper() {
        if (AuthorityUtils.authorityListToSet(userDetails.getAuthorities()).contains("CREATE_CLIENT"))
            return;
        userDetailsMapper.changeRoleByRoleName(userDetails.getUid(), "ROLE_DEV");
    }

    public String generateAvatarUrl(Long uid, Integer size, Long t) {
        String key = uimProperties.getStorage().getStoragePath() + "avatar/" + uid;
        if (!storage.isExist(key)) {
            // 头像不存在
            return null;
        }
        String urlString = uimProperties.getStorage().getStorageBaseUrl() == null ?
                storage.generateGetUrl(key, 1000L * 60L * 60 * 24L) + "&" :
                uimProperties.getStorage().getStorageBaseUrl() + key + "?";
        if (size != null)
            urlString += "imageMogr2/thumbnail/" + size + "x" + size;
        if (t != null)
            urlString += "&t=" + t;
        return urlString;
    }
}