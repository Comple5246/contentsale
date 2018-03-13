package com.netease.comple.contentsale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netease.comple.contentsale.datatransferobject.CartContentInfo;
import com.netease.comple.contentsale.entity.User;
import com.netease.comple.contentsale.service.CartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    Logger logger = LogManager.getLogger(CartController.class.getName());

    @Autowired
    CartService cartService;

    @RequestMapping("/settleAccount")
    String cart(HttpServletRequest req, HttpServletResponse response, HttpSession session) throws Exception{
        User user = (User) session.getAttribute("user");
        if (null == user)
            return "redirect:/login";
        List<CartContentInfo>  cartContentInfos = (List<CartContentInfo>) session.getAttribute("cartContents");
        if (null != cartContentInfos) {
            logger.debug(cartContentInfos.size() + " cart contents got!");
            List<Map<String, Object>> cartContentViews = new LinkedList<Map<String, Object>>();
            for (CartContentInfo cartContentInfo : cartContentInfos) {
                Map<String, Object> cartContentView = new HashMap<String, Object>();
                cartContentView.put("id", cartContentInfo.getId());
                cartContentView.put("title", cartContentInfo.getTitle());
                cartContentView.put("num", cartContentInfo.getNum());
                cartContentView.put("price", cartContentInfo.getPrice());
                cartContentViews.add(cartContentView);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(cartContentViews);
            String urlEncodedJsonString = URLEncoder.encode(jsonString, "utf-8");
            logger.debug("cart content list json: " + jsonString);
            logger.debug("URL encoded json：" + urlEncodedJsonString);
            Cookie messageCookie = new Cookie("products", urlEncodedJsonString);
            //Cookie只用作传递消息
            //messageCookie.setMaxAge(0);
            response.addCookie(messageCookie);
        }
        return "settleAccount";
    }

    @RequestMapping("/api/addCartContent")
    @ResponseBody
    Map<String, Object> addCartContent(@RequestBody Map<String, Object> jsonObject, HttpSession session, HttpServletResponse response ){

        Map<String, Object> result = new HashMap<String, Object>();

        User user = (User)session.getAttribute("user");
        if (null == user){
            response.setStatus(400);
            result.put("code", 400);
            result.put("message", "请先登录");
            return result;
        }
        if (null != jsonObject){

            int contentId = (Integer) jsonObject.get("id");

            List<CartContentInfo> cartContentInfos = (List<CartContentInfo>) session.getAttribute("cartContents");
            if (null == cartContentInfos){
                CartContentInfo cartContentInfo = new CartContentInfo();
                cartContentInfo.setBuyerId(user.getId());
                cartContentInfo.setId((Integer) jsonObject.get("id"));
                Object price = jsonObject.get("price");
                if (price instanceof Integer)
                    cartContentInfo.setPrice((Integer)jsonObject.get("price"));
                else
                    cartContentInfo.setPrice((Double) jsonObject.get("price"));
                cartContentInfo.setNum((Integer)jsonObject.get("num"));
                cartContentInfo.setTitle((String)jsonObject.get("title"));
                List<CartContentInfo> listToSet = new LinkedList<CartContentInfo>();
                listToSet.add(cartContentInfo);
                session.setAttribute("cartContents", listToSet);
            }else{
                boolean flag = false;
                for (CartContentInfo cartContentInfo : cartContentInfos){
                    if (cartContentInfo.getId() == contentId) {
                        cartContentInfo.setNum(cartContentInfo.getNum() + ((Integer) jsonObject.get("num")));
                        flag = true;
                        break;
                    }
                }
                if (!flag){
                    CartContentInfo cartContentInfo = new CartContentInfo();
                    cartContentInfo.setBuyerId(user.getId());
                    cartContentInfo.setId((Integer) jsonObject.get("id"));
                    Object price = jsonObject.get("price");
                    if (price instanceof Integer)
                        cartContentInfo.setPrice((Integer)jsonObject.get("price"));
                    else
                        cartContentInfo.setPrice((Double) jsonObject.get("price"));
                    cartContentInfo.setNum((Integer)jsonObject.get("num"));
                    cartContentInfo.setTitle((String)jsonObject.get("title"));
                    cartContentInfos.add(cartContentInfo);
                }
            }
            response.setStatus(200);
            result.put("code", 200);
            result.put("message", "加入购物车成功");
            return result;
        }else{
            response.setStatus(400);
            result.put("code", 400);
            result.put("message", "无商品添加");
            return result;
        }
    }
}
