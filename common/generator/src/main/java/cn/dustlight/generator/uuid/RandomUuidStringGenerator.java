package cn.dustlight.generator.uuid;

import cn.dustlight.generator.UniqueGenerator;

import java.util.UUID;

public class RandomUuidStringGenerator implements UniqueGenerator<String> {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
