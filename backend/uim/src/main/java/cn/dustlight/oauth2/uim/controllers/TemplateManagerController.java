package cn.dustlight.oauth2.uim.controllers;

import cn.dustlight.oauth2.uim.RestfulConstants;
import cn.dustlight.oauth2.uim.RestfulResult;
import cn.dustlight.oauth2.uim.entities.TemplateNode;
import cn.dustlight.oauth2.uim.services.TemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class TemplateManagerController implements ITemplateManagerController {

    @Autowired
    private TemplateManager templateManager;

    @Override
    public RestfulResult<List<String>> getTemplateNames() throws IOException {
        return RestfulResult.success(templateManager.getTemplatesName());
    }

    @Override
    public RestfulResult<List<TemplateNode>> getTemplates() throws IOException {
        return RestfulResult.success(templateManager.getTemplatesList());
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

    @Override
    public RestfulResult updateName(Long id, String name) {
        templateManager.updateName(id, name);
        return RestfulConstants.SUCCESS;
    }
}
