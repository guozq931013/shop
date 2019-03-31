package com.netease.service.impl;

import com.netease.constant.StandardCode;
import com.netease.dao.GoodsMapper;
import com.netease.model.Goods;
import com.netease.model.User;
import com.netease.service.GoodsService;
import com.netease.service.SoldGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private SoldGoodsService soldGoodsService;

    @Override
    public Goods getGoodsById(Integer goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    @Override
    public List<Goods> getGoodsListWithFlag(HttpServletRequest request) {

        User user= (User) request.getSession().getAttribute("user");
        List<Goods> allGoods = getGoodsList();

        if (user != null && user.getUsertype() == 0) {
            List<Integer> soldGoodsIdList = soldGoodsService.getSoldGoodsIdListByUserId(user.getId());
            for (Goods goods : allGoods) {
                if (soldGoodsIdList.contains(goods.getId())) {
                    goods.setFlag(StandardCode.BOUGHT_FLAG_CODE);
                }
            }
        } else if (user != null && user.getUsertype() == 1) {
            for (Goods goods : allGoods) {
                if (goods.getQuantitySold() > 0) {
                    goods.setFlag(StandardCode.SOLD_FLAG_CODE);
                }
            }
        }

        return allGoods;
    }

    @Override
    public List<Goods> getGoodsList() {
        return goodsMapper.selectAllGoods();
    }

    @Override
    public void insertGoods(Goods goods) {
        goodsMapper.insertSelective(goods);
    }

    @Override
    public void updateGoods(Goods goods) {
        goodsMapper.updateByPrimaryKeySelective(goods);
    }

    @Override
    public Double getPriceById(Integer goodsId) {
        return goodsMapper.selectPriceByPrimaryKey(goodsId);
    }

    @Override
    public Integer getSoldQuantityById(Integer goodsId) {
        return goodsMapper.selectSoldQuantityByPrimaryKey(goodsId);
    }

    @Override
    public List<Goods> getSoldGoodsByUserId(Integer userId) {
        return goodsMapper.selectSoldGoodsByUserId(userId);
    }

    @Override
    public void remove(Integer goodsId) {
        goodsMapper.deleteByPrimaryKey(goodsId);
    }
}
