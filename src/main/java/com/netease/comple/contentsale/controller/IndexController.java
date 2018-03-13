package com.netease.comple.contentsale.controller;

import com.netease.comple.contentsale.datatransferobject.ContentInfo;
import com.netease.comple.contentsale.entity.User;
import com.netease.comple.contentsale.service.ContentService;
import com.netease.comple.contentsale.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    private Logger logger = LogManager.getLogger(IndexController.class.getName());

    @Autowired
    ContentService contentService;

    @RequestMapping("/")
    public String index(HttpServletRequest req, HttpSession session){
        logger.debug("/ hit");
        User user = (User)session.getAttribute("user");
        if (null == user){
            //没有用户登录
            List<ContentInfo> allContents = contentService.getAllContents();
            req.setAttribute("productList", allContents);
        }else if (0 == user.getUserType()){
            //用户是买家
            String type = req.getParameter("type");
            if (StringUtil.isNullorEmpty(type) || 1 != Integer.parseInt(type)){
                List<ContentInfo> allContentsWithBuyingInfo = contentService.getAllContentsWithBuyingInfo(user.getId());
                req.setAttribute("productList", allContentsWithBuyingInfo);
            }else{
                List<ContentInfo> unboughtContents = contentService.getAllUnboughtContents(user.getId());
                logger.debug("/ hit: unbought contents size: " + unboughtContents.size());
                req.setAttribute("productList", unboughtContents);
            }
        }else if(1 == user.getUserType()){
            //用户是卖家
            List<ContentInfo> allContentsWithSellingInfo = contentService.getAllContentsWithSellingInfo(user.getId());
            req.setAttribute("productList", allContentsWithSellingInfo);
        }
        return "index";
    }
}
