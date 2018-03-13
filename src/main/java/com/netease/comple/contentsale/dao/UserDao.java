package com.netease.comple.contentsale.dao;

import com.netease.comple.contentsale.entity.User;

public interface UserDao {

    User getUserById(int id);

    User getUserByName(String userName);
}
