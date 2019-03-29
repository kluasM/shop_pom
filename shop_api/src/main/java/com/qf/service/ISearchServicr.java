package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface ISearchServicr {

    List<Goods> queryByIndexId(String keyword);

    int insertIndexed(Goods goods);

}
