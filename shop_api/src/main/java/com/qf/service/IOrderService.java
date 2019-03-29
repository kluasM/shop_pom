package com.qf.service;

import com.qf.entity.Orders;
import com.qf.entity.User;

import java.util.List;

public interface IOrderService {
    int addorder(Integer aid, Integer[] cids, User user);
    List<Orders> queryByUid(Integer uid);
}
