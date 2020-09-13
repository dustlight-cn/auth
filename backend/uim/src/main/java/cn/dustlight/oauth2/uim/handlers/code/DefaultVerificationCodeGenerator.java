package cn.dustlight.oauth2.uim.handlers.code;

import java.security.SecureRandom;
import java.util.Random;

public class DefaultVerificationCodeGenerator implements VerificationCodeGenerator {

    private Random random = new SecureRandom();

    @Override
    public String generatorCode(int length) {
        return generatorCode(length, null);
    }

    @Override
    public String generatorCode(int length, char[] extendChars) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int type = random.nextInt(extendChars == null || extendChars.length == 0 ?
                    3 : 4);
            char c;
            switch (type) {
                default:
                case 0:
                    c = (char) ('a' + random.nextInt(26));
                    break;
                case 1:
                    c = (char) ('A' + random.nextInt(26));
                    break;
                case 2:
                    c = (char) ('0' + random.nextInt(10));
                    break;
                case 3:
                    c = extendChars[random.nextInt(extendChars.length)];
            }
            builder.append(c);
        }
        return builder.toString();
    }

}
