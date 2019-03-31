package com.netease.vo;

import com.netease.model.SoldGoods;

import java.util.Date;
import java.util.List;

public class OrderItemVO {

    private Integer id;

    private Double amount;

    private Date orderTime;

    private Integer userId;

    private List<SoldGoods> soldGoodsList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<SoldGoods> getSoldGoodsList() {
        return soldGoodsList;
    }

    public void setSoldGoodsList(List<SoldGoods> soldGoodsList) {
        this.soldGoodsList = soldGoodsList;
    }
}
