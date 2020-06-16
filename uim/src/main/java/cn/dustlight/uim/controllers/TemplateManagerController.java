package cn.dustlight.uim.controllers;

import cn.dustlight.sender.core.ITemplateManager;
import cn.dustlight.uim.RestfulConstants;
import cn.dustlight.uim.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class TemplateManagerController implements ITemplateManagerController {

    @Autowired
    private ITemplateManager templateManager;

    @Override
    public RestfulResult<List<String>> getTemplateNames() throws IOException {
        return RestfulResult.success(templateManager.getTemplatesName());
    }

    @Override
    public RestfulResult<String> getTemplate(String name) throws IOException {
        return RestfulResult.success(templateManager.getTemplate(name));
    }

    @Override
    public RestfulResult setTemplate(String name, String text) throws IOException {
        templateManager.setTemplate(name, text);
        return RestfulConstants.SUCCESS;
    }

    @Override
    public RestfulResult deleteTemplate(String[] names) throws IOException {
        templateManager.deleteTemplate(names);
        return RestfulConstants.SUCCESS;
    }
}
