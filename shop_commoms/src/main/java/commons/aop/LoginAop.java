package commons.aop;

import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

@Aspect
public class LoginAop {
    @Autowired
    private RedisTemplate redisTemplate;
    @Around("@annotation(IsLogin)")
    public Object isLogin(ProceedingJoinPoint proceedingJoinPoint){
        //判断当前用户是否登陆
        //获得HttpServletRequest
        ServletRequestAttributes requestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token=null;
        Cookie[] cookies=request.getCookies();
        if (cookies != null) {
            for (Cookie cookie:cookies) {
                if (cookie.getName().equals("Login_cookie")) {
                    token=cookie.getValue();
                    break;
                }
            }
        }

        User user=null;
        //根据token从redis中获取用户信息
        if (token != null) {
            user= (User) redisTemplate.opsForValue().get(token);
        }

        if (user == null) {
            MethodSignature signature= (MethodSignature) proceedingJoinPoint.getSignature();
            //获得目标方法的Method对象
            Method method = signature.getMethod();
            //从目标方法上获得注解
            IsLogin isLogin = method.getAnnotation(IsLogin.class);
            //获得注解的方法返回值
            boolean flag = isLogin.toLogin();
            if (flag) {
                //获得当前URL
                String returnURL=request.getRequestURL()+ "?" + request.getQueryString();
                try {
                    returnURL= URLEncoder.encode(returnURL, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //强制跳转到登陆页
                return "redirect:http://localhost:8086/sso/tologin?returnURL="+returnURL;
            }
        }
        //中间运行目标方法,同时让目标方法中的一个User形参，变成当前登陆用户的信息
        //获取原来的参数
        Object[] args = proceedingJoinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i]!=null&&args[i].getClass()==User.class){
                args[i]=user;
            }
        }
        Object result=null;
        try {
            result=proceedingJoinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return result;
    }
}
