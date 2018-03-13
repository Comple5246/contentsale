package com.netease.comple.contentsale.entity;

import java.util.Date;

public class Transaction {
    private int id;
    private int buyerId;
    private Content content;
    private double buyPrice;
    private int num;
    private long buyTime;

    public Content getContent() {
        return content;
    }

    public void setContentId(Content content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public double getBuyPrice() { return buyPrice; }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(long buyTime) {
        this.buyTime = buyTime;
    }
}
