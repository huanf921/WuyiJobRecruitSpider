package com.vs.wuyijobrecruitspider.service;

import com.vs.wuyijobrecruitspider.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User getUser(String username);

    List<Integer> getJobIdListByUserId(Integer userId);

    void saveUserStar(Integer jobId, Integer userId);

    void removeUserStar(Integer jobId, Integer userId);

    List<Map<String, Object>> getJobListByUserId(Integer userId);
}
