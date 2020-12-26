package cn.dustlight.auth.generator.uuid;

import cn.dustlight.auth.generator.UniqueGenerator;

import java.util.UUID;

/**
 * UUID 生成器
 */
public class RandomUuidGenerator implements UniqueGenerator<UUID> {

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }

}
