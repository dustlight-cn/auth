package cn.dustlight.generator.uuid;

import cn.dustlight.generator.UniqueGenerator;

import java.util.UUID;

public class RandomUuidGenerator implements UniqueGenerator<UUID> {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
