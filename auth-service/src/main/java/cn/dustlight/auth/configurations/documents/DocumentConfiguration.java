package cn.dustlight.auth.configurations.documents;

import cn.dustlight.auth.util.Constants;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Auth Service",
                description = "提供身份管理服务以及OAuth2授权服务。身份管理服务包含用户管理、角色管理、权限管理等，OAuth2授权服务包含应用管理、应用授权模式与授权作用域管理等。",
                version = Constants.VERSION,
                contact = @Contact(name = "Hansin1997",
                        email = "845612500@qq.com",
                        url = "https://github.com/Hansin1997"),
                license = @License(name = "Apache License", url = "http://www.apache.org/licenses/LICENSE-2.0.txt")
        ),
        externalDocs = @ExternalDocumentation(description = "View on Github", url = "https://github.com/Hansin1997/auth")
)
@SecurityScheme(name = "Client Credentials", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER, scheme = "basic")
@SecurityScheme(name = "Access Token", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.QUERY, paramName = "access_token")
public class DocumentConfiguration {
}
