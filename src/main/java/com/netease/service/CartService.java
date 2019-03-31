package com.netease.service;


import com.netease.model.CartItem;
import com.netease.model.User;
import com.netease.vo.CartItemVO;

import java.util.List;

public interface CartService {

    void addGoods(Integer goodsId, Integer quantity, User user);

    List<CartItem> getCartItemListByUserId(Integer userId);

    List<CartItemVO> getCartItemVOListByUserId(Integer userId);

    Double getCartTotalAmountByUserId(Integer userId);

    void clear(User user);

    int remove(Integer cartItemId, User user);
}
