package com.netease.comple.contentsale.service;

import com.netease.comple.contentsale.datatransferobject.ContentInfo;
import com.netease.comple.contentsale.entity.Content;

import java.util.List;

public interface ContentService {

    List<ContentInfo> getAllContents();

    List<ContentInfo> getAllContentsWithBuyingInfo(int buyerId);

    List<ContentInfo> getAllContentsWithSellingInfo(int sellerId);

    List<ContentInfo> getAllBoughtContents(int buyerId);

    List<ContentInfo> getAllUnboughtContents(int buyerId);

    boolean deleteContentById(int contentId);

    List<ContentInfo> getContentWithBuyPriceNumInfo(int contentId, int buyerId);

    ContentInfo getContentById(int contentId);

    boolean addContent(Content content);

    boolean updateContent(Content content);
}
