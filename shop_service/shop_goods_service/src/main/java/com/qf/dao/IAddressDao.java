package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Address;

public interface IAddressDao extends BaseMapper<Address> {
    int insertAddr(Address address);
}
