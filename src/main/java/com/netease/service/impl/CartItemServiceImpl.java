package com.netease.service.impl;

import com.netease.dao.CartItemMapper;
import com.netease.model.CartItem;
import com.netease.service.CartItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Resource
    private CartItemMapper cartItemMapper;

    @Override
    public void addCartItem(CartItem cartItem) {
        cartItemMapper.insert(cartItem);
    }

    @Override
    public List<CartItem> getListByUserId(Integer userId) {
        return cartItemMapper.selectListByUserId(userId);
    }

    @Override
    public void modify(CartItem cartItem) {
        cartItemMapper.updateByPrimaryKeySelective(cartItem);
    }

    @Override
    public CartItem getByGoodsIdAndUserId(Integer goodsId, Integer userId) {
        return cartItemMapper.selectByGoodsIdAndUserId(goodsId, userId);
    }

    @Override
    public void deleteAllByUserId(Integer userId) {
        cartItemMapper.deleteByUserId(userId);
    }

    @Override
    public void deleteById(Integer cartItemId) {
        cartItemMapper.deleteByPrimaryKey(cartItemId);
    }

    @Override
    public CartItem getById(Integer cartItemId) {
        return cartItemMapper.selectByPrimaryKey(cartItemId);
    }
}
