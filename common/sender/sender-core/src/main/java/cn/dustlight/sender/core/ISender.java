package cn.dustlight.sender.core;

import java.io.IOException;
import java.util.Map;

public interface ISender {

    void send(String template,
              Map<String, String> data,
              Map<String, Object> parameters,
              String... receivers) throws IOException;

}
