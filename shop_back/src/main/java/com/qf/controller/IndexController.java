package com.qf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author LJM
 * @Date 2019/1/16
 */
@Controller
public class IndexController {

    @RequestMapping("/toPage/{page}")
    public String toPage(@PathVariable("page") String page){
        return page;
    }
}
