package cn.dustlight.oauth2.uim.handlers.code;

import cn.dustlight.generator.Generator;

public interface VerificationCodeGenerator extends Generator<String> {

    String generateCode(int length);

    String generateCode(int length, char[] extendChars);

}
