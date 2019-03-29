package com.qf.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IAddressDao;
import com.qf.entity.Address;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressDao addressDao;
    @Override
    public List<Address> queryAddressByUid(Integer uid) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("uid",uid);
        return addressDao.selectList(queryWrapper);
    }

    /**
     * 使用存储过程完成地址添加
     * @param address
     * @return
     */
    @Override
    public int insertAddress(Address address) {

        return addressDao.insertAddr(address);
    }
}
