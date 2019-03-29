package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Orders implements Serializable {
    @TableId(type = IdType.AUTO)
    private int id;
    private String orderid;
    private int uid;
    private String person;
    private String address;
    private int code;
    private String phone;
    private double oprice;
    private int status;//0-未支付 1-已支付/待发货 2-已发货/待收货 3-已收货/待评价 4-已评价
    private Date ordertime;
    @TableField(exist = false)
    private List<OrderDetils> orderDetils;

}
