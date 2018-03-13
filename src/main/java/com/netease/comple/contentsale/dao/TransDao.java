package com.netease.comple.contentsale.dao;

import com.netease.comple.contentsale.entity.Transaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransDao {

    Integer getTransCountByContentId(int contentId);

    Integer getTransCountByContentIdAndBuyerId(@Param("contentId") int contentId, @Param("buyerId") int buyerId);

    List<Transaction> getAllTransByBuyerId(int buyerId);

    List<Transaction> getAllTransByContentIdAndBuyerId(@Param("contentId") int contentId, @Param("buyerId") int buyerId);

    int addTransaction(@Param("contentId") int contentId, @Param("buyerId") int buyerId, @Param("buyPrice") double buyPrice, @Param("num") int num, @Param("buyTime") long buyTime);
}
