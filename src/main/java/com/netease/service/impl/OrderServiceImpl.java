package com.netease.service.impl;

import com.netease.dao.OrderMapper;
import com.netease.model.CartItem;
import com.netease.model.Goods;
import com.netease.model.Order;
import com.netease.model.SoldGoods;
import com.netease.service.CartItemService;
import com.netease.service.GoodsService;
import com.netease.service.OrderService;
import com.netease.service.SoldGoodsService;
import com.netease.vo.OrderItemVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SoldGoodsService soldGoodsService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private CartItemService cartItemService;

    @Override
    public List<OrderItemVO> getOrderItemVOListByUserId(Integer userId) {

        List<OrderItemVO> orderItemVOList = new ArrayList<>();

        List<Order> orderList = getListByUserId(userId);
        for (Order order : orderList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            orderItemVO.setAmount(order.getAmount());
            orderItemVO.setUserId(userId);
            orderItemVO.setId(order.getId());
            orderItemVO.setSoldGoodsList(soldGoodsService.getListByOrderId(order.getId()));
            orderItemVOList.add(orderItemVO);
        }

        return orderItemVOList;
    }

    @Override
    public List<Order> getListByUserId(Integer userId) {
        return orderMapper.selectListByUserId(userId);
    }

    @Override
    public Double getOrderTotalAmountByUserId(Integer userId) {

        List<Order> orderList = getListByUserId(userId);
        double totalAmount = 0;
        for (Order order : orderList) {
            totalAmount += order.getAmount();
        }

        BigDecimal tempAmount = new BigDecimal(totalAmount);
        totalAmount = tempAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return totalAmount;
    }

    @Override
    @Transactional
    public void generateOrder(CartItem cartItem, Integer userId) {

        Integer cartItemId = cartItem.getId();
        Integer goodsId = cartItem.getGoodsId();
        Integer quantity = cartItem.getQuantity();
        double goodsPrice = goodsService.getPriceById(goodsId);

        Order order = new Order();
        order.setAmount(quantity * goodsPrice);
        order.setUserId(userId);
        order.setOrderTime(new Date());
        addOrder(order);

        int orderId = order.getId();

        Goods goods = goodsService.getGoodsById(goodsId);
        SoldGoods soldGoods = new SoldGoods();
        soldGoods.setDescription(goods.getDescription());
        soldGoods.setGoodsId(goodsId);
        soldGoods.setOrderId(orderId);
        soldGoods.setSummary(goods.getSummary());
        soldGoods.setTitle(goods.getTitle());
        soldGoods.setQuantity(quantity);
        soldGoods.setPrice(goodsPrice);
        soldGoods.setSumPrice(goodsPrice * quantity);
        soldGoods.setPicture(goods.getPicture());

        soldGoodsService.addSoldGoods(soldGoods);
        cartItemService.deleteById(cartItemId);
        goods.setQuantitySold((goods.getQuantitySold() == null ? 0 : goods.getQuantitySold()) + quantity);
        goodsService.updateGoods(goods);
    }

    @Override
    @Transactional
    public void batchGenerateOrder(Integer[] cartItemIds, Integer[] goodsIds, Integer[] quantities, Integer userId) {

        int orderSize = cartItemIds.length;
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderTime(new Date());

        double amount = 0;
        for (int i = 0; i < orderSize; i++) {
            amount += (quantities[i] * goodsService.getPriceById(goodsIds[i]));
        }
        order.setAmount(amount);
        addOrder(order);
        int orderId = order.getId();

        for (int i = 0; i < orderSize; i++) {

            double goodsPrice = goodsService.getPriceById(goodsIds[i]);
            Goods goods = goodsService.getGoodsById(goodsIds[i]);
            SoldGoods soldGoods = new SoldGoods();
            soldGoods.setGoodsId(goodsIds[i]);
            soldGoods.setOrderId(orderId);
            soldGoods.setTitle(goods.getTitle());
            soldGoods.setSummary(goods.getSummary());
            soldGoods.setPrice(goodsPrice);
            soldGoods.setPicture(goods.getPicture());
            soldGoods.setDescription(goods.getDescription());
            soldGoods.setQuantity(quantities[i]);
            soldGoods.setSumPrice(goodsPrice * quantities[i]);

            soldGoodsService.addSoldGoods(soldGoods);
            cartItemService.deleteById(cartItemIds[i]);
            goods.setQuantitySold((goods.getQuantitySold() == null ? 0 : goods.getQuantitySold()) + quantities[i]);
            goodsService.updateGoods(goods);
        }
    }

    @Override
    public void addOrder(Order order) {
        orderMapper.insertSelective(order);
    }
}
