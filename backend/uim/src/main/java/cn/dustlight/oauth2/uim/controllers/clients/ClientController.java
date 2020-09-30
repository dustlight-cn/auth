package cn.dustlight.oauth2.uim.controllers.clients;

import cn.dustlight.oauth2.uim.Constants;
import cn.dustlight.oauth2.uim.entities.results.QueryResults;
import cn.dustlight.oauth2.uim.entities.v1.clients.Client;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "应用管理业务", description = "应用的增删改查")
@RequestMapping(value = Constants.V1.API_ROOT, produces = Constants.ContentType.APPLICATION_JSON)
public interface ClientController {

    @Operation(summary = "查询应用")
    @GetMapping("clients")
    QueryResults<? extends Client, ? extends Number> getClients(@RequestParam(required = false) String keywords,
                                                                @RequestParam(required = false) Collection<String> order,
                                                                @RequestParam(required = false) Integer offset,
                                                                @RequestParam(required = false) Integer limit);

    @Operation(summary = "删除应用")
    @DeleteMapping("clients")
    void removeClients(@RequestParam Collection<String> cids);

    @Operation(summary = "获取应用")
    @GetMapping("client/{cid}")
    Client getClient(@PathVariable String cid);

    @Operation(summary = "删除应用")
    @DeleteMapping("client/{cid}")
    void removeClient(@PathVariable String cid);

    @Operation(summary = "创建应用")
    @PostMapping("client")
    Client createClient(@RequestParam Long uid,
                        @RequestParam String name,
                        @RequestParam String description,
                        @RequestParam String redirectUri,
                        @RequestParam Collection<Long> scopes,
                        @RequestParam Collection<Long> grantTypes,
                        @RequestParam Integer accessTokenValidity,
                        @RequestParam Integer refreshTokenValidity,
                        @RequestParam String additionalInformation,
                        @RequestParam Integer status);
}
