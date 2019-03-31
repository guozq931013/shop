package com.netease.service;

import com.netease.model.CartItem;
import com.netease.model.Order;
import com.netease.vo.OrderItemVO;

import java.util.List;

public interface OrderService {

    List<OrderItemVO> getOrderItemVOListByUserId(Integer userId);

    List<Order> getListByUserId(Integer userId);

    Double getOrderTotalAmountByUserId(Integer userId);

    //选中一件商品是下单
    void generateOrder(CartItem cartItem, Integer buyerId);

    //批量下单
    void batchGenerateOrder(Integer[] cartItemIds, Integer[] goodsIds, Integer[] quantities, Integer buyerId);

    void addOrder(Order order);
}
