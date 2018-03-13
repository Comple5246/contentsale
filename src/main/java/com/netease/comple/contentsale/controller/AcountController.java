package com.netease.comple.contentsale.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.netease.comple.contentsale.datatransferobject.TransactionInfo;
import com.netease.comple.contentsale.entity.Transaction;
import com.netease.comple.contentsale.entity.User;
import com.netease.comple.contentsale.service.TransService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class AcountController {

    Logger logger = LogManager.getLogger(AcountController.class.getName());

    @Autowired
    TransService transService;

    @RequestMapping("/account")
    String account(HttpServletRequest req, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (null != user){
            req.setAttribute("buyList", transService.getAllTransByBuyerId(user.getId()));
            return "account";
        }else
            return "redirect:/login";
    }

    @RequestMapping("/api/buy")
    @ResponseBody
    Map<String, Object> buy(@RequestBody List<Map<String, Object>> jsonObjects, HttpServletResponse response, HttpSession session) throws Exception{

        logger.debug(jsonObjects.size() + " cart contents to buy.");

        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<String, Object>();
        if (null == user){
            result.put("code", 400);
            result.put("message", "请重新登录！");
            response.setStatus(400);
        }
        List<TransactionInfo> transactionInfos = new LinkedList<TransactionInfo>();
        for (Map<String, Object> jsonObject : jsonObjects){
            TransactionInfo transactionInfo = new TransactionInfo();
            transactionInfo.setBuyTime(new Date().getTime());
            Object price = jsonObject.get("price");
            if (price instanceof Double)
                transactionInfo.setBuyPrice((Double) price);
            else
                transactionInfo.setBuyPrice((Integer) price);
            transactionInfo.setContentId((Integer) jsonObject.get("id"));
            transactionInfo.setNum((Integer) jsonObject.get("number"));
            transactionInfo.setBuyerId(user.getId());
            transactionInfos.add(transactionInfo);
        }
        //返回的没有成功添加到trx中的交易
        List<TransactionInfo> transactionInfosLeft = transService.addAllTransactions(transactionInfos);
        session.removeAttribute("cartContents");

        //购买成功
        result.put("code", 200);
        result.put("message", "购买成功");
        return result;
    }
}
