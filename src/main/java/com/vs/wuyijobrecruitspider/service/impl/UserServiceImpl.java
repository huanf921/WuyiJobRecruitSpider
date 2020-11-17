package com.vs.wuyijobrecruitspider.service.impl;

import com.vs.wuyijobrecruitspider.dao.UserDao;
import com.vs.wuyijobrecruitspider.entity.JobInfo;
import com.vs.wuyijobrecruitspider.entity.User;
import com.vs.wuyijobrecruitspider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(String username) {
        List<User> users = userDao.findByUsernameIs(username);
        log.info(users.toString());
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<Integer> getJobIdListByUserId(Integer userId) {
        List<Integer> jobIdList = userDao.findJobIdList(userId);
        return jobIdList;
    }

    @Override
    public void saveUserStar(Integer jobId, Integer userId) {
        userDao.saveUserStar(jobId, userId);
    }

    @Override
    public void removeUserStar(Integer jobId, Integer userId) {
        userDao.removeUserStar(jobId, userId);
    }

    @Override
    public List<Map<String, Object>> getJobListByUserId(Integer userId) {
        List<Map<String, Object>> jobList = userDao.findJobList(userId);
        return jobList;
    }

    @Override
    public Integer getJobsByAddr(String address) {

        return userDao.findJobsByAddr(address);
    }
}
