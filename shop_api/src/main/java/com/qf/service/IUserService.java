package com.qf.service;

import com.qf.entity.User;

public interface IUserService {
    User queryByUserNameAndPassword(String username,String password);

}
