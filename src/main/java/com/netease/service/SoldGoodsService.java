package com.netease.service;

import com.netease.model.SoldGoods;

import java.util.List;

public interface SoldGoodsService {

    List<Integer> getSoldGoodsIdListByUserId(Integer userId);

    List<SoldGoods> getListByUserId(Integer userId);

    void addSoldGoods(SoldGoods soldGoods);

    List<SoldGoods> getListByOrderId(Integer orderId);
}
