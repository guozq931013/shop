package com.netease.service.impl;

import com.netease.constant.StandardCode;
import com.netease.model.CartItem;
import com.netease.model.Goods;
import com.netease.model.User;
import com.netease.service.CartItemService;
import com.netease.service.CartService;
import com.netease.service.GoodsService;
import com.netease.vo.CartItemVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private CartItemService cartItemService;

    @Resource
    private GoodsService goodsService;

    @Override
    public void addGoods(Integer goodsId, Integer quantity, User user) {

        CartItem cartItem = cartItemService.getByGoodsIdAndUserId(goodsId, user.getId());

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setGoodsId(goodsId);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItemService.addCartItem(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemService.modify(cartItem);
        }
    }

    @Override
    public List<CartItem> getCartItemListByUserId(Integer userId) {
        return cartItemService.getListByUserId(userId);
    }

    @Override
    public List<CartItemVO> getCartItemVOListByUserId(Integer userId) {

        List<CartItem> cartItemList = getCartItemListByUserId(userId);
        List<CartItemVO> cartItemVOList = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {

            int quantity = cartItem.getQuantity();
            Goods goods = goodsService.getGoodsById(cartItem.getGoodsId());
            if (goods != null && goods.getId() != null) {
                CartItemVO cartItemVO = new CartItemVO();
                cartItemVO.setId(cartItem.getId());
                cartItemVO.setTitle(goods.getTitle());
                cartItemVO.setGoodsId(goods.getId());
                cartItemVO.setPicture(goods.getPicture());
                cartItemVO.setPrice(goods.getPrice());
                cartItemVO.setQuantity(quantity);
                cartItemVO.setSubtotal(quantity * goods.getPrice());

                cartItemVOList.add(cartItemVO);
            }
        }

        return cartItemVOList;
    }

    @Override
    public Double getCartTotalAmountByUserId(Integer userId) {

        List<CartItem> cartItemList = getCartItemListByUserId(userId);
        double totalAmount = 0;
        for (CartItem cartItem : cartItemList) {
            Double price = goodsService.getPriceById(cartItem.getGoodsId());
            totalAmount += (price * cartItem.getQuantity());
        }

        BigDecimal tempAmount = new BigDecimal(totalAmount);
        totalAmount = tempAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return totalAmount;
    }

    @Override
    public void clear(User user) {
        cartItemService.deleteAllByUserId(user.getId());
    }

    @Override
    public int remove(Integer cartItemId, User user) {

        CartItem cartItem = cartItemService.getById(cartItemId);
        if (cartItem != null && cartItem.getUserId().intValue() == user.getId()) {
            cartItemService.deleteById(cartItemId);
            return StandardCode.QUERY_SUCCESS_CODE;
        }

        return StandardCode.QUERY_FAILED_CODE;
    }
}