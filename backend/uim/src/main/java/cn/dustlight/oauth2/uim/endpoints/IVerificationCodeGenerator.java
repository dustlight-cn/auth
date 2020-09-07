package cn.dustlight.oauth2.uim.endpoints;

public interface IVerificationCodeGenerator {

    String generatorCode(int length);

    String generatorCode(int length, char[] extendChars);

}
