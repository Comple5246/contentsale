package com.netease.comple.contentsale.service.impl;

import com.netease.comple.contentsale.datatransferobject.CartContentInfo;
import com.netease.comple.contentsale.dao.CartDao;
import com.netease.comple.contentsale.entity.CartContent;
import com.netease.comple.contentsale.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    CartDao cartDao;

    public List<CartContentInfo> getAllCartContentsByBuyerId(int buyerId){
        List<CartContent> cartContents = cartDao.getAllCartContentsByBuyerId(buyerId);
        List<CartContentInfo> result = new LinkedList<CartContentInfo>();
        if (null != cartContents){
            for (CartContent cartContent : cartContents){
                CartContentInfo cartContentInfo = new CartContentInfo();
                cartContentInfo.setBuyerId(buyerId);
                cartContentInfo.setId(cartContent.getContent().getId());
                cartContentInfo.setContent(cartContent.getContent().getContent());
                cartContentInfo.setImage(cartContent.getContent().getImage());
                cartContentInfo.setNum(cartContent.getNum());
                cartContentInfo.setPrice(cartContent.getContent().getPrice());
                cartContentInfo.setSellerId(cartContent.getContent().getSellerId());
                cartContentInfo.setSummary(cartContent.getContent().getSummary());
                cartContentInfo.setTitle(cartContent.getContent().getTitle());
                result.add(cartContentInfo);
            }
        }
        cartDao.deleteAllCartContentsByBuyerId(buyerId);
        return result;
    }

    public void addCartContentInfos(List<CartContentInfo> cartContentInfos){
        for (CartContentInfo cartContentInfo : cartContentInfos){
            cartDao.addCartContent(cartContentInfo.getBuyerId(), cartContentInfo.getId(), cartContentInfo.getNum());
        }
    }
}
