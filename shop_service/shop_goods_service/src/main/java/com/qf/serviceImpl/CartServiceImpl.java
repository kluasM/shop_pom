package com.qf.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.ICartDao;
import com.qf.dao.IGoodsDao;
import com.qf.entity.Cart;
import com.qf.entity.Goods;
import com.qf.entity.User;
import com.qf.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private ICartDao cartDao;
    @Autowired
    private IGoodsDao goodsDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public String addCart(Cart cart, User user, String cartToken) {
        if (user != null) {
            cart.setUid(user.getId());
            cartDao.insert(cart);
        }else {
            if(cartToken==null){
               cartToken= UUID.randomUUID().toString();
            }
            redisTemplate.opsForList().leftPush(cartToken,cart);

        }
        return cartToken;
    }

    /**
     * 合并购物车
     * @param cartToken
     * @param user
     * @return
     */
    @Override
    public int mergeCart(String cartToken, User user) {
        if (cartToken != null && redisTemplate.opsForList().size(cartToken)>0) {
            //有临时购物车

            //将临时购物车的数据取出放入数据库中
            List<Cart> range = redisTemplate.opsForList().range(cartToken, 0, redisTemplate.opsForList().size(cartToken));
            for (Cart cart : range) {
                //设置临时购物车的所属者
                cart.setUid(user.getId());
                //保存进数据库
                cartDao.insert(cart);
            }
            //清空临时购物车
            redisTemplate.delete(cartToken);
            return 1;
        }
        return 0;
    }

    @Override
    public List<Cart> queryCartList(String cartToken, User user) {
        List<Cart> cartList=null;
        if (user != null) {
            //已登录-数据库
            cartList=cartDao.queryCartsByUid(user.getId());
        }else {
            //未登录-redis
            if (cartToken != null && redisTemplate.opsForList().size(cartToken)>0) {
                cartList=redisTemplate.opsForList().range(cartList,0,redisTemplate.opsForList().size(cartToken));
                //根据临时购物车数据，从数据库中查询商品信息
                for (Cart cart:cartList) {
                    //根据购物车数据，查询商品信息
                    QueryWrapper queryWrapper=new QueryWrapper();
                    queryWrapper.eq("id",cart.getGid());
                    Goods goods = goodsDao.selectOne(queryWrapper);
                    cart.setGoods(goods);
                }
            }
        }
        return cartList;
    }
}
