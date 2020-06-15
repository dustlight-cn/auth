package cn.dustlight.uim.services;

import java.io.IOException;
import java.util.Map;

public interface IEmailSender {

    void send(String templateName, Map<String,Object> parameters, String... receivers) throws IOException;
}
