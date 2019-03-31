package com.netease.service;

import com.netease.model.CartItem;

import java.util.List;

public interface CartItemService {

    void addCartItem(CartItem cartItem);

    List<CartItem> getListByUserId(Integer userId);

    void modify(CartItem cartItem);

    CartItem getByGoodsIdAndUserId(Integer goodsId, Integer userId);

    void deleteAllByUserId(Integer userId);

    void deleteById(Integer cartItemId);

    CartItem getById(Integer cartItemId);
}
