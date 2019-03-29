package com.qf.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IUserDao;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Override
    public User queryByUserNameAndPassword(String username, String password) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        queryWrapper.eq("password",password);

        return userDao.selectOne(queryWrapper);
    }
}
