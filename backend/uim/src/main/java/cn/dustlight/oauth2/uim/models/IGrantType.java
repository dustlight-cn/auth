package cn.dustlight.oauth2.uim.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

public interface IGrantType extends Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    Long getId();

    String getName();
}
