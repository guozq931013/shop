package com.netease.dao;

import com.netease.model.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    List<Goods> selectAllGoods();

    Double selectPriceByPrimaryKey(@Param("id") Integer id);

    Integer selectSoldQuantityByPrimaryKey(@Param("id") Integer id);

    List<Goods> selectSoldGoodsByUserId(@Param("userId") Integer userId);
}
