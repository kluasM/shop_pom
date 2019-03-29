package com.qf.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String name;
    private int age;
    private Date birthday;
    private String idcard;
    private String phone;
    private String email;
}
