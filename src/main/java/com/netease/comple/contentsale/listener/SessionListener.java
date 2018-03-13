package com.netease.comple.contentsale.listener;

import com.netease.comple.contentsale.datatransferobject.CartContentInfo;
import com.netease.comple.contentsale.entity.User;
import com.netease.comple.contentsale.service.CartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

public class SessionListener implements HttpSessionListener {

    private static Logger logger = LogManager.getLogger(SessionListener.class.getName());

    public void sessionCreated(HttpSessionEvent httpSessionEvent) { }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //Session销毁时，将购物车内容存入数据库
        HttpSession session = httpSessionEvent.getSession();
        User user = (User) session.getAttribute("user");
        if (null != user && user.getUserType() == 0){
            CartService cartService = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()).getBean(CartService.class);
            List<CartContentInfo> cartContentInfos = (List<CartContentInfo>) session.getAttribute("cartContents");
            if (null != cartContentInfos) {
                logger.debug("write " + cartContentInfos.size() + " cart contents into database");
                cartService.addCartContentInfos(cartContentInfos);
            }
        }
    }
}
