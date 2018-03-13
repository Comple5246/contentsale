package com.netease.comple.contentsale.service.impl;

import com.netease.comple.contentsale.datatransferobject.ContentInfo;
import com.netease.comple.contentsale.dao.ContentDao;
import com.netease.comple.contentsale.dao.TransDao;
import com.netease.comple.contentsale.entity.Content;
import com.netease.comple.contentsale.entity.Transaction;
import com.netease.comple.contentsale.service.ContentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class ContentServiceImpl implements ContentService{

    private static Logger logger = LogManager.getLogger(ContentServiceImpl.class.getName());

    @Autowired
    ContentDao contentDao;

    @Autowired
    TransDao transDao;

    @Autowired
    ServletContext servletContext;

    public List<ContentInfo> getAllContents(){
        List<Content> contents = contentDao.getAllContents();
        List<ContentInfo> result = new LinkedList();
        if (null != contents) {
            for (Content content : contents) {
                ContentInfo contentInfo = new ContentInfo();
                contentInfo.setId(content.getId());
                contentInfo.setPrice(content.getPrice());
                contentInfo.setTitle(content.getTitle());
                contentInfo.setSummary(content.getSummary());
                contentInfo.setContent(content.getContent());
                contentInfo.setImage(content.getImage());
                contentInfo.setSellerId(content.getSellerId());
                result.add(contentInfo);
            }
        }
        return result;
    }

    public List<ContentInfo> getAllContentsWithBuyingInfo(int buyerId) {
        List<Content> contents = contentDao.getAllContents();
        List<ContentInfo> result = new LinkedList();
        if (null != contents) {
            for (Content content : contents) {
                Integer count = transDao.getTransCountByContentIdAndBuyerId(content.getId(), buyerId);
                ContentInfo contentInfo = new ContentInfo();
                contentInfo.setId(content.getId());
                contentInfo.setPrice(content.getPrice());
                contentInfo.setTitle(content.getTitle());
                contentInfo.setSummary(content.getSummary());
                contentInfo.setContent(content.getContent());
                contentInfo.setSellerId(content.getSellerId());
                contentInfo.setImage(content.getImage());
                if (null != count && count > 0)
                    contentInfo.setIsBuy(true);
                else
                    contentInfo.setIsBuy(false);
                result.add(contentInfo);
            }
        }
        return result;
    }

    public List<ContentInfo> getAllContentsWithSellingInfo(int sellerId){
        List<Content> contents = contentDao.getAllContentsBySellerId(sellerId);
        List<ContentInfo> result = new LinkedList();
        if (null != contents) {
            for (Content content : contents) {
                Integer count = transDao.getTransCountByContentId(content.getId());
                ContentInfo contentInfo = new ContentInfo();
                contentInfo.setId(content.getId());
                contentInfo.setPrice(content.getPrice());
                contentInfo.setTitle(content.getTitle());
                contentInfo.setSummary(content.getSummary());
                contentInfo.setContent(content.getContent());
                contentInfo.setSellerId(content.getSellerId());
                contentInfo.setImage(content.getImage());
                if (null != count && count > 0)
                    contentInfo.setIsSell(true);
                else
                    contentInfo.setIsSell(false);
                result.add(contentInfo);
            }
        }
        return result;
    }

    public List<ContentInfo> getAllBoughtContents(int buyerId){
        List<Content> contents = contentDao.getAllBoughtContentsByBuyerId(buyerId);
        List<ContentInfo> result = new LinkedList<ContentInfo>();
        if (null != contents){
            for (Content content : contents) {
                ContentInfo contentInfo = new ContentInfo();
                contentInfo.setId(content.getId());
                contentInfo.setPrice(content.getPrice());
                contentInfo.setTitle(content.getTitle());
                contentInfo.setSummary(content.getSummary());
                contentInfo.setContent(content.getContent());
                contentInfo.setSellerId(content.getSellerId());
                contentInfo.setImage(content.getImage());
                contentInfo.setIsBuy(true);
                result.add(contentInfo);
            }
        }
        return result;
    }

    public List<ContentInfo> getAllUnboughtContents(int buyerId) {
        List<Content> contents = contentDao.getAllUnboughtContentsByBuyerId(buyerId);
        List<ContentInfo> result = new LinkedList<ContentInfo>();
        if (null != contents){
            for (Content content : contents){
                ContentInfo contentInfo = new ContentInfo();
                contentInfo.setId(content.getId());
                contentInfo.setPrice(content.getPrice());
                contentInfo.setTitle(content.getTitle());
                contentInfo.setSummary(content.getSummary());
                contentInfo.setContent(content.getContent());
                contentInfo.setSellerId(content.getSellerId());
                contentInfo.setImage(content.getImage());
                contentInfo.setIsBuy(false);
                result.add(contentInfo);
            }
        }
        return result;
    }

    public boolean deleteContentById(int contentId){
        Content content = contentDao.getContentById(contentId);
        if (null == content){
            logger.debug("No such content, id = " + contentId);
            return false;
        }
        String imageLocation = content.getImage();
        if (imageLocation.startsWith("/")){
            //图像文件是上传的
            File imageFile = new File(servletContext.getRealPath(imageLocation));
            if (imageFile.exists())
                imageFile.delete();
            imageFile = null;
        }
        int numOfContentDeleted = contentDao.deleteContentById(contentId);
        logger.debug("One content deleted, id = " + contentId);
        return true;
    }

    public List<ContentInfo> getContentWithBuyPriceNumInfo(int contentId, int buyerId){
        List<Transaction> transactions = transDao.getAllTransByContentIdAndBuyerId(contentId, buyerId);
        List<ContentInfo> result = new LinkedList<ContentInfo>();
        if (null != transactions){
            for (Transaction transaction : transactions){
                Content content = transaction.getContent();
                ContentInfo contentInfo = new ContentInfo();
                contentInfo.setId(content.getId());
                contentInfo.setPrice(content.getPrice());
                contentInfo.setTitle(content.getTitle());
                contentInfo.setSummary(content.getSummary());
                contentInfo.setContent(content.getContent());
                contentInfo.setSellerId(content.getSellerId());
                contentInfo.setImage(content.getImage());
                contentInfo.setIsBuy(true);
                contentInfo.setBuyPrice(transaction.getBuyPrice());
                contentInfo.setNum(transaction.getNum());
                result.add(contentInfo);
            }
        }
        return result;
    }

    public ContentInfo getContentById(int contentId) {
        Content content = contentDao.getContentById(contentId);
        if (null == content)
            return null;

        ContentInfo contentInfo = new ContentInfo();
        contentInfo.setId(content.getId());
        contentInfo.setPrice(content.getPrice());
        contentInfo.setTitle(content.getTitle());
        contentInfo.setSummary(content.getSummary());
        contentInfo.setContent(content.getContent());
        contentInfo.setSellerId(content.getSellerId());
        contentInfo.setImage(content.getImage());
        return contentInfo;
    }

    public boolean addContent(Content content){
        if (contentDao.addContent(content) > 0)
            return true;
        else
            return false;
    }

    public boolean updateContent(Content content){
        if (contentDao.updateContent(content) > 0)
            return true;
        return false;
    }
}
