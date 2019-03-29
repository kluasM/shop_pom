package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.Cart;
import com.qf.entity.Orders;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrderService;
import commons.aop.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Reference
    private IAddressService addressService;
    @Reference
    private ICartService cartService;
    @Reference
    private IOrderService orderService;
    @IsLogin(toLogin = true)
    @RequestMapping("/edit")
    public String edit(Integer[] gid , User user, Model model){
        //根据登陆用户，获取该用户所有的收获地址
        List<Address> addresses=addressService.queryAddressByUid(user.getId());
        //根据勾选的商品ID，找出对应的购物车信息
        //所有购物车的数据
        List<Cart> cartList = cartService.queryCartList(null, user);
        //需要下单的购物车数据
        List<Cart> cartList1=new ArrayList<>();
        for (Cart cart: cartList){
            for (Integer id:gid){
                if (cart.getGid() == id){
                    cartList1.add(cart);
                }
            }
        }
        model.addAttribute("addresses",addresses);
        model.addAttribute("carts",cartList1);
        return "orderedit";
    }
    /**
     * 添加订单
     */
    @RequestMapping("/insert")
    @ResponseBody
    @IsLogin
    public int insertOrder(Integer aid,@RequestParam("cids[]") Integer[] cids,User user){

        return orderService.addorder(aid,cids,user);
    }
    @RequestMapping("/showlist")
    @IsLogin(toLogin = true)
    public String showList(User user,Model model){
        List<Orders> orders = orderService.queryByUid(user.getId());
        model.addAttribute("orders",orders);
        return "orderlist";
    }
}
