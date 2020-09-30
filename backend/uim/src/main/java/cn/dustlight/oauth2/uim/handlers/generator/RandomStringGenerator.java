package cn.dustlight.oauth2.uim.handlers.generator;

import cn.dustlight.generator.Generator;

import java.security.SecureRandom;
import java.util.Random;

public class RandomStringGenerator implements Generator<String> {

    public final static char[] DEFAULT_CHARS = "0123456789qwertyuioplkjhgfdsazxcvbnm".toCharArray();
    public final static int DEFAULT_LENGTH = 6;
    private Random random = new SecureRandom();

    private char[] chars;
    private int length;

    public RandomStringGenerator(char[] chars, int length) {
        this.chars = chars;
        this.length = length;
    }

    public RandomStringGenerator() {
        this.chars = DEFAULT_CHARS;
        this.length = DEFAULT_LENGTH;
    }

    @Override
    public String generate() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++)
            builder.append(chars[random.nextInt(chars.length)]);
        return builder.toString();
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setChars(char[] chars) {
        this.chars = chars;
    }

    public int getLength() {
        return length;
    }

    public char[] getChars() {
        return chars;
    }
}
