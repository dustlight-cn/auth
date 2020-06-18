package cn.dustlight.uim.services;

public interface IVerificationCodeGenerator {

    String generatorCode(int length);

    String generatorCode(int length, char[] extendChars);

}
