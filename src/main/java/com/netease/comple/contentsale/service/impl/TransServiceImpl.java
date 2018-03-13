package com.netease.comple.contentsale.service.impl;

import com.netease.comple.contentsale.datatransferobject.TransactionInfo;
import com.netease.comple.contentsale.dao.TransDao;
import com.netease.comple.contentsale.entity.Transaction;
import com.netease.comple.contentsale.service.TransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class TransServiceImpl implements TransService{

    @Autowired
    TransDao transDao;

    public List<TransactionInfo> getAllTransByBuyerId(int buyerId){
        List<Transaction> transactions = transDao.getAllTransByBuyerId(buyerId);
        List<TransactionInfo> result = new LinkedList<TransactionInfo>();
        if (null != transactions){
            for (Transaction trans : transactions){
                TransactionInfo transactionInfo = new TransactionInfo();
                transactionInfo.setId(trans.getId());
                transactionInfo.setContentId(trans.getContent().getId());
                transactionInfo.setTitle(trans.getContent().getTitle());
                transactionInfo.setImage(trans.getContent().getImage());
                transactionInfo.setBuyTime(trans.getBuyTime());
                transactionInfo.setBuyPrice(trans.getBuyPrice());
                transactionInfo.setNum(trans.getNum());
                result.add(transactionInfo);
            }
        }
        return result;
    }

    public List<TransactionInfo> addAllTransactions(List<TransactionInfo> transactionInfos){
        Iterator<TransactionInfo> iterator = transactionInfos.iterator();
        while (iterator.hasNext()){
            TransactionInfo transactionInfo = iterator.next();
            if (transDao.addTransaction(transactionInfo.getContentId(), transactionInfo.getBuyerId(), transactionInfo.getBuyPrice(), transactionInfo.getNum(), transactionInfo.getBuyTime()) > 0){
                iterator.remove();
            }
        }
        return transactionInfos;
    }
}
