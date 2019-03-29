package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("/sso")
public class LoginController {
    @Reference
    private IUserService userService;
    @Reference
    private ICartService cartService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("/tologin")
    public String toLogin(String returnURL,Model model){
        model.addAttribute("returnURL",returnURL);
        return "login";
    }
    @RequestMapping("/login")
    public String Login(
                        @CookieValue(value = "cart_cookie",required = false)String cartToken,
                        String username,
                        String password,
                        HttpServletResponse response,
                        Model model,String returnURL){
        User user=userService.queryByUserNameAndPassword(username,password);
        if (user!=null){
            //调用购物车服务，合并临时购物车
            cartService.mergeCart(cartToken,user);
            if (returnURL==null || "".equals(returnURL)){
                returnURL = "http://localhost:8082";
            }
            String uuid= UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(uuid,user);
            //回写Cookie到用户的浏览器
            Cookie cookie=new Cookie("Login_cookie",uuid);
            cookie.setMaxAge(60*60*24*30);
            cookie.setPath("/");
            response.addCookie(cookie);
            System.out.println("登陆成功！");
            return "redirect:" + returnURL;
        }
        System.out.println("登陆失败！");
        model.addAttribute("error","用户名或密码错误！");
       return "login";
    }
    @RequestMapping("/islogin")
    @ResponseBody
    @CrossOrigin
    public String isLogin(@CookieValue(value = "Login_cookie",required = false)String cookie){
        System.out.println("获得浏览器中的cookie值："+cookie);
        User user=null;
        if (cookie!=null){
            //通过UUID验证redis中是否有用户信息
            user= (User) redisTemplate.opsForValue().get(cookie);
        }
        return user!=null? "islogin("+ new Gson().toJson(user)+")":"islogin(null)";

    }

    /**
     * 注销
     * @param cookie
     * @return
     */
    @RequestMapping("/logout")
    public String logout(@CookieValue(value = "Login_cookie",required = false)String cookie,HttpServletResponse response){
        //删除redis
        redisTemplate.delete(cookie);
        //删除Cookie
        Cookie cookie1=new Cookie("Login_cookie",null);
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        return "login";
    }
}
