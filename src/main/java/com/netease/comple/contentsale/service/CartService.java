package com.netease.comple.contentsale.service;

import com.netease.comple.contentsale.datatransferobject.CartContentInfo;

import java.util.List;

public interface CartService {

    List<CartContentInfo> getAllCartContentsByBuyerId(int buyerId);

    void addCartContentInfos(List<CartContentInfo> contentInfos);
}
