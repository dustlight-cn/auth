package cn.dustlight.oauth2.uim.handlers.email;

import java.io.IOException;
import java.util.Map;

public interface EmailSenderHandler {

    void send(String templateName, Map<String,Object> parameters, String... receivers) throws IOException;
}
