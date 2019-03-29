package com.qf.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class Address implements Serializable {
    private int id;
    private String person;
    private String address;
    private String phone;
    private int code;
    private int uid;
    private int isdefault=0;
}
