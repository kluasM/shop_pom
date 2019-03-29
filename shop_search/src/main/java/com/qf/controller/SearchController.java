package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.entity.Goods;
import com.qf.entity.Page;
import com.qf.service.ISearchServicr;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/search")
public class SearchController {

    @Reference
    private ISearchServicr searchServicr;

    @RequestMapping("/query")
    public String query(String keywork, Model model){


        List<Goods> goods = searchServicr.queryByIndexId(keywork);



        model.addAttribute("goodsList",goods);

        return "searchList";
    }
}
