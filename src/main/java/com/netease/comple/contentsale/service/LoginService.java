package com.netease.comple.contentsale.service;

import com.netease.comple.contentsale.entity.User;

public interface LoginService {

    User getUser(String userName, String password);
}
