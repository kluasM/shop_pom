package com.qf.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IAddressDao;
import com.qf.dao.ICartDao;
import com.qf.dao.IOrderDao;
import com.qf.dao.IOrderDetilDao;
import com.qf.entity.*;
import com.qf.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IAddressDao addressDao;
    @Autowired
    private ICartDao cartDao;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IOrderDetilDao orderDetilDao;
    @Override
    @Transactional
    public int addorder(Integer aid, Integer[] cids ,User user) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("id",aid);
        Address address=addressDao.selectOne(queryWrapper);
        QueryWrapper queryWrapper1=new QueryWrapper();
        queryWrapper1.in("id",cids);
        List<Cart> cartList=cartDao.queryCartsByIds(cids);
        double allPrice=0;
        for (Cart cart:cartList){
            allPrice+=cart.getGnumber()*cart.getGoods().getPrice();
        }
        Orders orders=new Orders();
        orders.setOrderid(UUID.randomUUID().toString());
        orders.setUid(user.getId());
        orders.setPerson(address.getPerson());
        orders.setAddress(address.getAddress());
        orders.setCode(address.getCode());
        orders.setOrdertime(new Date());
        orders.setPerson(address.getPhone());
        orders.setStatus(0);
        orders.setOprice(allPrice);
        orderDao.insert(orders);
        for (Cart cart:cartList){
            OrderDetils orderDetils=new OrderDetils();
            orderDetils.setOid(orders.getId());
            orderDetils.setGid(cart.getGid());
            orderDetils.setGname(cart.getGoods().getTitle());
            orderDetils.setGinfo(cart.getGoods().getGinfo());
            orderDetils.setGprice(cart.getGoods().getPrice());
            orderDetils.setGimage(cart.getGoods().getGimage());
            orderDetilDao.insert(orderDetils);
        }
        cartDao.deleteBatchIds(Arrays.asList(cids));
        return 1;
    }

    @Override
    public List<Orders> queryByUid(Integer uid) {
        //查询当前用户的所有订单
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("uid",uid);
        List<Orders> list = orderDao.selectList(queryWrapper);
        //根据订单ID查询订单详情
        for (Orders order:list){
            QueryWrapper queryWrapper1=new QueryWrapper();
            queryWrapper1.eq("oid",order.getId());
            List selectList = orderDetilDao.selectList(queryWrapper1);
            order.setOrderDetils(selectList);
        }

        return list;
    }
}
