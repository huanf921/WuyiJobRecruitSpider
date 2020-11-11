package com.vs.wuyijobrecruitspider.dao;

import com.vs.wuyijobrecruitspider.entity.User;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserDao extends Repository<User, Integer> {
    List<User> findByUsernameIs(String username);
}
