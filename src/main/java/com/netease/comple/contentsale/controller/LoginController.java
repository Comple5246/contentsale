package com.netease.comple.contentsale.controller;


import com.netease.comple.contentsale.datatransferobject.CartContentInfo;
import com.netease.comple.contentsale.entity.User;
import com.netease.comple.contentsale.service.CartService;
import com.netease.comple.contentsale.service.LoginService;
import com.netease.comple.contentsale.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController{

    private static Logger logger = LogManager.getLogger(LoginController.class.getName());

    @Autowired
    LoginService loginService;

    @Autowired
    CartService cartService;

    @RequestMapping("/login")
    public String login(HttpServletRequest req, HttpSession session){
        User user = (User) session.getAttribute("user");
        logger.debug("/login hit");
        if (null == user)
            return "login";
        else
            return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        //跳转到主界面
        return "redirect:/";
    }

    @RequestMapping("/api/login")
    @ResponseBody
    Map<String, Object> apiLogin(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws Exception{
        User userInSesion = (User) session.getAttribute("user");
        if (null == userInSesion){
            //还没登录，处理登录
            logger.debug("/api/login hit: haven't login");

            String userName = req.getParameter("userName");
            String password = req.getParameter("password");
            if (!StringUtil.isNullorEmpty(userName) && !StringUtil.isNullorEmpty(password)) {
                User user = loginService.getUser(userName, password);
                if (null == user) {
                    //用户名或者密码错误
                    logger.debug("/api/login hit: wrong username or password");

                    Map<String, Object> result = new HashMap();
                    result.put("code", new Integer(400));
                    result.put("message", "账号密码错误");
                    return result;
                }
                //将用户加入到Session中，实现免密登录
                session.setAttribute("user", user);
            }else{
                //账号密码为空
                logger.debug("/api/login hit: empty username or password");

                Map<String, Object> result = new HashMap();
                result.put("code", new Integer(400));
                result.put("message", "账号密码不能为空");
                return result;
            }
        }

        //登录成功后将购物车内容取出存放在session中
        userInSesion = (User) session.getAttribute("user");
        if (userInSesion.getUserType() == 0){
            List<CartContentInfo> cartContentInfos = cartService.getAllCartContentsByBuyerId(userInSesion.getId());
            logger.debug(cartContentInfos.size() + " cart contents got!");
            if (!cartContentInfos.isEmpty())
                session.setAttribute("cartContents", cartContentInfos);
        }

        //成功登陆
        logger.debug("/api/login hit: login");
        Map<String, Object> result = new HashMap();
        result.put("code", new Integer(200));
        result.put("message", "success");

        return result;
    }
}
