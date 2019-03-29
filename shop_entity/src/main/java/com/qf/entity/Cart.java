package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("shopcart")
public class Cart implements Serializable{
    private int id;
    private int gid;
    private int gnumber;
    private int uid;
    @TableField(exist = false)
    private Goods goods;
}
