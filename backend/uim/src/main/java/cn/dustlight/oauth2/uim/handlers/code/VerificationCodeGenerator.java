package cn.dustlight.oauth2.uim.handlers.code;

public interface VerificationCodeGenerator {

    String generatorCode(int length);

    String generatorCode(int length, char[] extendChars);

}
