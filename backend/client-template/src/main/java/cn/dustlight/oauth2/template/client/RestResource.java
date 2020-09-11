package cn.dustlight.oauth2.template.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class RestResource {

    @Autowired
    private OAuth2RestOperations restTemplate;

    @RequestMapping("request")
    public void request(@RequestParam String url, @RequestParam(required = false) ResponseType type, HttpServletResponse response) throws IOException {
        try {
            ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
            if (!entity.getStatusCode().is2xxSuccessful()) {
                response.sendError(entity.getStatusCode().value(), entity.getStatusCode().getReasonPhrase());
                return;
            }
            switch (type) {
                case html:
                    response.setContentType("text/html;charset=utf-8");
                    break;
                case json:
                    response.setContentType("application/json;charset=utf-8");
                    break;
                case text:
                default:
                    response.setContentType("text/plain;charset=utf-8");
            }
            response.getWriter().println(entity.getBody());
        } catch (Exception e) {
            response.setStatus(500);
            e.printStackTrace(response.getWriter());
        }
    }

    public static enum ResponseType {
        json,
        text,
        html
    }
}
