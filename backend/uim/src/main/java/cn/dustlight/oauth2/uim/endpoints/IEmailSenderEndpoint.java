package cn.dustlight.oauth2.uim.endpoints;

import java.io.IOException;
import java.util.Map;

public interface IEmailSenderEndpoint {

    void send(String templateName, Map<String,Object> parameters, String... receivers) throws IOException;
}
