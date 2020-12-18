package cn.dustlight.auth.configurations.components;

import cn.dustlight.auth.generator.Generator;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.generator.others.RandomStringGenerator;
import cn.dustlight.auth.generator.others.UniqueLongToStringGenerator;
import cn.dustlight.auth.generator.snowflake.SnowflakeIdGenerator;
import cn.dustlight.auth.properties.SnowflakeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@EnableConfigurationProperties(SnowflakeProperties.class)
public class GeneratorConfiguration {

    @Bean("snowflakeIdGenerator")
    @ConditionalOnMissingBean(name = "snowflakeIdGenerator")
    public SnowflakeIdGenerator snowflakeIdGenerator(@Autowired SnowflakeProperties properties) throws SocketException {
        long machineId, dataCenterId;
        if (properties.getMachineId() == null) {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e;
            e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                sb.append(ni.toString());
            }
            machineId = sb.toString().hashCode();
            if (machineId < 0)
                machineId = -machineId;
            if (machineId > SnowflakeIdGenerator.MAX_MACHINE_NUM)
                machineId = machineId & SnowflakeIdGenerator.MAX_MACHINE_NUM;
        } else {
            machineId = properties.getMachineId();
        }
        if (properties.getDataCenterId() == null) {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            dataCenterId = Long.parseLong(runtimeMXBean.getName().split("@")[0]);
            if (dataCenterId < 0)
                dataCenterId = -dataCenterId;
            if (dataCenterId > SnowflakeIdGenerator.MAX_DATACENTER_NUM)
                dataCenterId = dataCenterId & SnowflakeIdGenerator.MAX_DATACENTER_NUM;
        } else {
            dataCenterId = properties.getDataCenterId();
        }
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(dataCenterId, machineId);
        if (properties.getStartTimestamp() != null)
            generator.setStartTimestamp(properties.getStartTimestamp());
        return generator;
    }

    @Bean("clientSecretGenerator")
    @ConditionalOnMissingBean(name = "clientSecretGenerator")
    public Generator<String> clientSecretGenerator() {
        RandomStringGenerator generator = new RandomStringGenerator();
        generator.setChars("0123456789abcdef".toCharArray());
        generator.setLength(40);
        return generator;
    }

    @Bean("clientIdGenerator")
    @ConditionalOnMissingBean(name = "clientIdGenerator")
    public UniqueGenerator<String> clientIdGenerator(@Autowired UniqueGenerator<Long> longUniqueGenerator) {
        UniqueLongToStringGenerator generator = new UniqueLongToStringGenerator(longUniqueGenerator);
        generator.setHex("0123456789abcdef".toCharArray());
        return generator;
    }
}
