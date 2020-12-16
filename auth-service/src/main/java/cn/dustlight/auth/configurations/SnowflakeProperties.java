package cn.dustlight.auth.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("dustlight.auth.service.snowflake")
public class SnowflakeProperties {

    private Long machineId, dataCenterId, startTimestamp;

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public Long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }
}
