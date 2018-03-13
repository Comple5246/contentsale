package com.netease.comple.contentsale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.comple.contentsale.datatransferobject.CartContentInfo;
import com.netease.comple.contentsale.datatransferobject.ContentInfo;
import com.netease.comple.contentsale.entity.Content;
import com.netease.comple.contentsale.entity.User;
import com.netease.comple.contentsale.service.ContentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class ContentController {

    Logger logger = LogManager.getLogger(ContentController.class.getName());

    @Autowired
    ContentService contentService;

    @RequestMapping("/api/delete")
    @ResponseBody
    Map<String, Object> deleteContent(HttpServletRequest req, HttpServletResponse response, HttpSession session){
        String contentIdString = req.getParameter("id");
        if (null == contentIdString){
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("code", 400);
            result.put("message", "请给定ID");
            return result;
        }else{
            int contentId;
            try {
                 contentId = Integer.parseInt(contentIdString);
            }catch (Exception ex){
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("code", 400);
                result.put("message", "ID只能是一个整数");
                return result;
            }
            ContentInfo contentInfo = contentService.getContentById(contentId);
            if (null == contentInfo){
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("code", 400);
                result.put("message", "没有请求内容");
                return result;
            }
            if (contentService.deleteContentById(contentId)){
                String image = contentInfo.getImage();
                if (image.startsWith("/")){
                    //删除图像文件
                    File imageFile = new File(session.getServletContext().getRealPath("") + image);
                    if (imageFile.exists()){
                        imageFile.delete();
                        imageFile = null;
                    }
                }
                response.setStatus(200);
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("code", 200);
                result.put("message", "success!");
                return result;
            }else{
                response.setStatus(400);
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("code", 400);
                result.put("message", "删除失败");
                return result;
            }
        }
    }

    @RequestMapping("/show")
    String show(HttpServletRequest req, HttpServletResponse response, HttpSession session) throws Exception{
        User user = (User) session.getAttribute("user");
        String contentIdString = req.getParameter("id");
        if (null != contentIdString){
            List<ContentInfo> contentInfos = null;
            if (null != user && user.getUserType() == 0 && !(contentInfos = contentService.getContentWithBuyPriceNumInfo(Integer.parseInt(contentIdString), user.getId())).isEmpty()) {
                req.setAttribute("product", contentInfos.get(0));
            }else{
                ContentInfo contentInfo = contentService.getContentById(Integer.parseInt(contentIdString));
                if (null != contentInfo)
                    req.setAttribute("product", contentInfo);
            }
        }
        return "show";
    }

    @RequestMapping("/public")
    public String publicContent(HttpServletRequest req, HttpSession session) throws Exception{
        User user = (User) session.getAttribute("user");
        if (null == user)
            return "redirect:/login";
        return "public";
    }

    @RequestMapping("/publicSubmit")
    public String publicSubmit(@RequestParam(value = "title") String title,
                               @RequestParam(value = "summary") String summary,
                               @RequestParam(value = "image") String image,
                               @RequestParam(value = "content") String content,
                               @RequestParam(value = "price") double price,
                               HttpServletRequest req,
                               HttpSession session) throws Exception{
        User user = (User) session.getAttribute("user");

        if (null == user)
            return "redirect:/login";
        Content con = new Content();
        con.setContent(content);
        con.setImage(image);
        con.setPrice(price);
        con.setSellerId(user.getId());
        con.setSummary(summary);
        con.setTitle(title);
        if(contentService.addContent(con))
            req.setAttribute("product", con);
        return "publicSubmit";
    }

    //图片文件上传
    @RequestMapping("/api/upload")
    @ResponseBody
    Map<String, Object> imageUpload(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException{
        String fileURI = null;
        if (!file.isEmpty()){
            String rootPath = session.getServletContext().getRealPath("");
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String contentType = file.getContentType();
            String suffix = contentType.substring(contentType.indexOf("/") + 1);
            fileURI= "/image/" + uuid + "." + suffix;
            File localfile = new File(rootPath + fileURI);
            while (localfile.exists()){
                uuid = UUID.randomUUID().toString().replace("-", "");
                fileURI= "/image/" + uuid + "." + suffix;
                localfile = new File(rootPath + fileURI);
            }
            logger.debug("image file uploaded, real path: " + localfile.getAbsolutePath());
            file.transferTo(localfile);
        }else{
            //默认图片文件
            fileURI= "/image/default.jpg";
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", fileURI);
        return map;
    }

    @RequestMapping("/edit")
    String edit(HttpServletRequest req, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (null == user)
            return "redirect:/login";
        int contentId = Integer.parseInt(req.getParameter("id"));
        ContentInfo contentInfo = contentService.getContentById(contentId);
        if (null != contentInfo)
            req.setAttribute("product", contentInfo);
        return "edit";
    }

    @RequestMapping("/editSubmit")
    public String editSubmit(@RequestParam(value = "id") int contentId,
                               @RequestParam(value = "title") String title,
                               @RequestParam(value = "summary") String summary,
                               @RequestParam(value = "image") String image,
                               @RequestParam(value = "content") String content,
                               @RequestParam(value = "price") double price,
                               HttpServletRequest req,
                               HttpSession session) throws Exception{
        User user = (User) session.getAttribute("user");
        if (null == user)
            return "redirect:/login";

//        logger.debug(req.getCharacterEncoding());
//
//        logger.debug(title);
//        logger.debug(URLEncoder.encode("测试", "utf-8"));
//        logger.debug(URLEncoder.encode(title, "ISO-8859-1"));
//        logger.debug(URLDecoder.decode(URLEncoder.encode("测试", "utf-8"),"utf-8"));
//        logger.debug(URLDecoder.decode(URLEncoder.encode(title, "ISO-8859-1"),"utf-8"));

        ContentInfo contentInfo = contentService.getContentById(contentId);
        if (null != contentInfo) {
            String preImage = contentInfo.getImage();
            if (!image.equals(preImage))
                if (preImage.startsWith("/")){
                    File preImageFile = new File(session.getServletContext().getRealPath("") + preImage);
                    if (preImageFile.exists()){
                        preImageFile.delete();
                        preImageFile = null;
                    }
                }
            Content con = new Content();
            con.setId(contentId);
            con.setContent(content);
            con.setImage(image);
            con.setPrice(price);
            con.setSellerId(user.getId());
            con.setSummary(summary);
            con.setTitle(title);
            if (contentService.updateContent(con)){
                Map<String, Object> productView = new HashMap<String, Object>();
                productView.put("id", con.getId());
                req.setAttribute("product", productView);
            }
        }
        return "editSubmit";
    }
}
