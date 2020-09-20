package cn.dustlight.validator.sender;

import cn.dustlight.validator.core.Code;

import java.util.Map;

/**
 * 验证码发送器
 */
public interface CodeSender<T> {

    void send(Code<T> code, Map<String, Object> parameters) throws SendCodeException;
}
