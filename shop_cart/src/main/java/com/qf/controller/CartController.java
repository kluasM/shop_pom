package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qf.entity.Cart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import commons.aop.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Reference
    private ICartService cartService;

    @IsLogin
    @RequestMapping("/add")
    public String CartAdd(@CookieValue(value = "cart_cookie",required = false)String cartToken,
                          Cart cart,
                          User user,
                          HttpServletResponse response){
        String cToken = cartService.addCart(cart, user, cartToken);
        if (cartToken==null){
            Cookie cookie=new Cookie("cart_cookie",cToken);
            cookie.setMaxAge(60*60*24*365);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return "success";
    }
    @RequestMapping("/showlist")
    @IsLogin
    @ResponseBody
    public String showlist(@CookieValue(value = "cart_cookie",required = false)String cartToken,User user){
        List<Cart> cartList = cartService.queryCartList(cartToken, user);
        return "cartlist(" + new Gson().toJson(cartList) + ")";
    }
    @RequestMapping("/cartlist")
    @IsLogin
   public String cartList(@CookieValue(value = "cart_cookie",required = false)String cartToken, User user, Model model){
        List<Cart> carts=cartService.queryCartList("cart_cookie",user);
        model.addAttribute("carts",carts);
        System.out.println("carts:" + carts);
       return "cartlist";
   }
}
