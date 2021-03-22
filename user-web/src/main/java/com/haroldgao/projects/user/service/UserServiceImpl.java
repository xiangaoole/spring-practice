package com.haroldgao.projects.user.service;

import com.haroldgao.log.Logger;
import com.haroldgao.projects.user.domain.User;
import com.haroldgao.projects.user.sql.LocalTransactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

public class UserServiceImpl implements UserService{

    @Resource(name = "bean/EntityManager")
    EntityManager entityManager;

    @Override
    @LocalTransactional
    public boolean register(User user) {
        try {
            entityManager.persist(user);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean queryUserById(long id) {
        return false;
    }

    @Override
    public boolean queryUserByNameAndPassword(String name, String password) {
        return false;
    }
}
