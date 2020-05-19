package cn.dustlight.uim.services;

import java.util.Map;

public interface IEmailSender {

    void Send(String templateName, Map<String,String> parameters,String... receivers);
}
