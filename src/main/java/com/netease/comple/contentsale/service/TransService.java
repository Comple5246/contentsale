package com.netease.comple.contentsale.service;

import com.netease.comple.contentsale.datatransferobject.CartContentInfo;
import com.netease.comple.contentsale.datatransferobject.TransactionInfo;

import java.util.List;

public interface TransService {
    List<TransactionInfo> getAllTransByBuyerId(int buyerId);

    List<TransactionInfo> addAllTransactions(List<TransactionInfo> transactionInfos);
}
