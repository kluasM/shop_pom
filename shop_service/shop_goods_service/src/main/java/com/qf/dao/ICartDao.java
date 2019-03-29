package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Cart;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ICartDao extends BaseMapper<Cart>{
    /**
     * 根据用户ID查询所有购物车信息
     */
    List<Cart> queryCartsByUid(Integer id);
    /**
     * 根据购物车id数组查询相应的购物车信息
     * @param cids
     * @return
     */
    List<Cart> queryCartsByIds(@Param("cids") Integer[] cids);
}
