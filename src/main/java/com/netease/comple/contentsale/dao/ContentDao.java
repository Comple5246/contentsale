package com.netease.comple.contentsale.dao;

import com.netease.comple.contentsale.entity.Content;

import java.util.List;

public interface ContentDao {

    Content getContentById(int contentId);

    List<Content> getAllContents();

    List<Content> getAllContentsBySellerId(int sellerId);

    List<Content> getAllBoughtContentsByBuyerId(int buyerId);

    List<Content> getAllUnboughtContentsByBuyerId(int buyerId);

    int deleteContentById(int contentId);

    int addContent(Content content);

    int updateContent(Content content);
}
