package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("orderdetils")
public class OrderDetils implements Serializable {
    private int id;
    private int oid;
    private int gid;
    private String gname;
    private String ginfo;
    private int gcount;
    private double gprice;
    private String gimage;
}
