package cn.dustlight.uim.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.util.Date;

public interface IRoleDetails extends Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    Long getId();

    String getName();

    String getDescription();

    Date getCreatedAt();

    Date GetUpdatedAt();
}
