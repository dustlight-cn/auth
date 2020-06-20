package cn.dustlight.uim.controllers;

import cn.dustlight.uim.RestfulResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/template")
public interface ITemplateManagerController {

    @GetMapping("/names")
    @PreAuthorize("hasAuthority('READ_TEMPLATE')")
    RestfulResult<List<String>> getTemplateNames() throws IOException;

    @GetMapping("/text/{name}")
    @PreAuthorize("hasAuthority('READ_TEMPLATE')")
    RestfulResult<String> getTemplate(@PathVariable String name) throws IOException;

    @PostMapping("/text/{name}")
    @PreAuthorize("hasAuthority('WRITE_TEMPLATE')")
    RestfulResult setTemplate(@PathVariable String name, @RequestParam String text) throws IOException;

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('WRITE_TEMPLATE')")
    RestfulResult deleteTemplate(@RequestBody String[] names) throws IOException;
}
