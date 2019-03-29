package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import commons.aop.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/addr")
public class AddressController {
    @Reference
    private IAddressService addressService;

    @RequestMapping("/insert")
    @ResponseBody
    @IsLogin(toLogin = true)
    public String insertAddr(Address address, User user){
        System.out.println("收货地址：" + address);
        address.setUid(user.getId());

        //调用service添加地址
        addressService.insertAddress(address);

        return null;
    }
}
