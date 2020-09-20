package cn.dustlight.validator.core;

import java.io.Serializable;
import java.util.Map;

public interface Code extends Serializable {

    Object getCode();

    Map<String, Object> getBody();
}
