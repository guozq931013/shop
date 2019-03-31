package com.netease.dao;

import com.netease.model.SoldGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SoldGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SoldGoods record);

    int insertSelective(SoldGoods record);

    SoldGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SoldGoods record);

    int updateByPrimaryKey(SoldGoods record);

    List<Integer> selectSoldGoodsIdListByUserId(@Param("userId") Integer userId);

    List<SoldGoods> selectListByUserId(@Param("userId") Integer userId);

    List<SoldGoods> selectByOrderId(Integer orderId);
}
