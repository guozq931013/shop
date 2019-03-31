package com.netease.model;

public class CartItem {
    private Integer id;

    private Integer goodsId;

    private Integer quantity;

    private Integer userId;

    public CartItem() {
    }

    public CartItem(Integer id, Integer goodsId, Integer quantity) {
        this.id = id;
        this.goodsId = goodsId;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
