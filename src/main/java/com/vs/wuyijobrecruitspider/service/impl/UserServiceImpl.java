package com.vs.wuyijobrecruitspider.service.impl;

import com.vs.wuyijobrecruitspider.dao.UserDao;
import com.vs.wuyijobrecruitspider.entity.User;
import com.vs.wuyijobrecruitspider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(String username) {
        List<User> users = userDao.findByUsernameIs(username);
        log.info(users.toString());
        return users.isEmpty() ? null : users.get(0);
    }
}
