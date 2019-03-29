package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName("goods")
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable{

    @TableId(type = IdType.AUTO)    // mybais plus主键回填策略
    private int id;
    private String title;
    private String ginfo;
    private int gcount;
    private int tid;//分类表的外键
    private double allprice;
    private double price;
    private String gimage;

}
