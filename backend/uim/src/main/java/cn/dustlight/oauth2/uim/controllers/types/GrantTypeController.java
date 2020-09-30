package cn.dustlight.oauth2.uim.controllers.types;

import cn.dustlight.oauth2.uim.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "授权模式管理业务", description = "授权模式的增删改查")
@RequestMapping(value = Constants.V1.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public interface GrantTypeController {
}
