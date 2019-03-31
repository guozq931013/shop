package com.netease.service.impl;

import com.netease.dao.SoldGoodsMapper;
import com.netease.model.SoldGoods;
import com.netease.service.SoldGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SoldGoodsServiceImpl implements SoldGoodsService {

    @Resource
    private SoldGoodsMapper soldGoodsMapper;

    @Override
    public List<Integer> getSoldGoodsIdListByUserId(Integer userId) {
        return soldGoodsMapper.selectSoldGoodsIdListByUserId(userId);
    }

    @Override
    public List<SoldGoods> getListByUserId(Integer userId) {
        return soldGoodsMapper.selectListByUserId(userId);
    }

    @Override
    public void addSoldGoods(SoldGoods soldGoods) {
        soldGoodsMapper.insertSelective(soldGoods);
    }

    @Override
    public List<SoldGoods> getListByOrderId(Integer orderId) {
        return soldGoodsMapper.selectByOrderId(orderId);
    }
}
