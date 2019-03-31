package com.netease.dao;

import com.netease.model.CartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CartItem record);

    int insertSelective(CartItem record);

    CartItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CartItem record);

    int updateByPrimaryKey(CartItem record);

    List<CartItem> selectListByUserId(@Param("userId") Integer userId);

    CartItem selectByGoodsIdAndUserId(@Param("goodsId")Integer goodsId, @Param("userId") Integer userId);

    void deleteByUserId(@Param("userId") Integer userId);
}
