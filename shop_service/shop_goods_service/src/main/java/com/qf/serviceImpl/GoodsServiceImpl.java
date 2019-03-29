package com.qf.serviceImpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IGoodsDao;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import com.qf.service.ISearchServicr;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private IGoodsDao goodsDao;

    @Reference
    private ISearchServicr searchServicr;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Goods> getGoodsList() {
        List<Goods> goods = goodsDao.selectList(new QueryWrapper<Goods>());
        System.out.println(goods);
        return goods;
    }

    @Override
    @Transactional      // 事务回滚
    public Goods addGoods(Goods goods) {
        // 将商品信息添加到数据库
        goodsDao.insert(goods);

        // 将商品信息添加到索引库
        searchServicr.insertIndexed(goods);

        //将goods对象发送到消息队列中
        rabbitTemplate.convertAndSend("goods_exchange","",goods);
        return goods;
    }
}
