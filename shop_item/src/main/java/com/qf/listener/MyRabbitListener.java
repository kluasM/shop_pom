package com.qf.listener;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = "goods_queue")
public class MyRabbitListener {
    @Autowired
    private Configuration configuration;
    @RabbitHandler
    public void handMsg(Goods goods){
        System.out.println("接收消息：" +goods);

        Map<String,Goods> map=new HashMap<>();
        map.put("goods",goods);

        String path=this.getClass().getResource("/static/page/").getPath()+goods.getId()+".html";
        System.out.println("静态页的生成路径："+path);

        try(
                Writer writer=new FileWriter(path)
        ){
            Template template = configuration.getTemplate("goods.ftl");
            template.process(map,writer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
