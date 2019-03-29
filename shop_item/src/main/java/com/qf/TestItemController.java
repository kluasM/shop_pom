package com.qf;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/Testitem")
public class TestItemController {
    @Autowired
    private Configuration configuration;
    @RequestMapping("/createHtml")
    public String createHtml() throws IOException, TemplateException {
        Template template = configuration.getTemplate("hello.ftl");
        Map map=new HashMap();
        map.put("name","Originals!");
        Writer writer=new FileWriter("G:\\hello.html");
        template.process(map,writer);
        writer.close();
        return "ok";
    }
}
