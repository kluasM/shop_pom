package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private IGoodsService goodsService;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Value("${fdfs.servicepath}")
    private String fdfsPath;

    @RequestMapping("/getGoodsList")
    public String getGoodsList(Model model){
        List<Goods> goodsList = goodsService.getGoodsList();
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("fdfsPath",fdfsPath);    //图片前缀
        return "goodsList";
    }

    @RequestMapping("/addGoods")
    public String addGoods(Goods go){
        System.out.println("进入到添加商品方法中！");
        Goods goods = goodsService.addGoods(go);
        return "redirect:/goods/getGoodsList";
    }

    @RequestMapping("/uploadimg")
    @ResponseBody
    public String uploadimg(MultipartFile file) throws IOException {
        System.out.println("图片名称:"+file.getOriginalFilename());
        System.out.println("图片大小:"+file.getSize());

        //将图片上传到FastDFS
        StorePath result = fastFileStorageClient.uploadImageAndCrtThumbImage(
                file.getInputStream(),
                file.getSize(),
                "png",
                null
        );
        System.out.println("图片路径:"+result.getFullPath());
        return "{\"imgpath\":\""+result.getFullPath()+"\"}";
    }
}
