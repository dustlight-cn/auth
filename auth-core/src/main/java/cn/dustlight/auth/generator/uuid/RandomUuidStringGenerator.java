package cn.dustlight.auth.generator.uuid;

import cn.dustlight.auth.generator.UniqueGenerator;

import java.util.UUID;

/**
 * UUID 字符串生成器
 */
public class RandomUuidStringGenerator implements UniqueGenerator<String> {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }

}
