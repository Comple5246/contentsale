package com.netease.comple.contentsale.service.impl;

import com.netease.comple.contentsale.dao.UserDao;
import com.netease.comple.contentsale.entity.User;
import com.netease.comple.contentsale.service.LoginService;
import com.netease.comple.contentsale.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService{

    private Logger logger = LogManager.getLogger(LoginServiceImpl.class.getName());

    @Autowired
    UserDao userDao;

    public User getUser(String userName, String password) {
        if (StringUtil.isNullorEmpty(userName) || StringUtil.isNullorEmpty(password))
            return null;
        User user = userDao.getUserByName(userName);
        if (null != user){
            logger.debug("User get from database: " + user.getUserName() + ", " + user.getPassword());
            String localPassword = user.getPassword();
            if (localPassword.equals(password))
                return user;
            else
                return null;
        }
        return null;
    }
}
