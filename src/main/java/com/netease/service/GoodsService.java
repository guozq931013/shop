package com.netease.service;

import com.netease.model.Goods;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GoodsService {

    Goods getGoodsById(Integer goodsId);

    List<Goods> getGoodsListWithFlag(HttpServletRequest request);

    List<Goods> getGoodsList();

    void insertGoods(Goods goods);

    void updateGoods(Goods goods);

    Double getPriceById(Integer goodsId);

    Integer getSoldQuantityById(Integer goodsId);

    List<Goods> getSoldGoodsByUserId(Integer userId);

    void remove(Integer goodsId);
}
