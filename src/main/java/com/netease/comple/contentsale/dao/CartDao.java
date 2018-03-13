package com.netease.comple.contentsale.dao;

import com.netease.comple.contentsale.entity.CartContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartDao {

    List<CartContent> getAllCartContentsByBuyerId(int buyerId);

    void deleteAllCartContentsByBuyerId(int buyerId);

    void addCartContent(@Param("buyerId")int buyerId, @Param("contentId") int contentId, @Param("num") int num);
}
