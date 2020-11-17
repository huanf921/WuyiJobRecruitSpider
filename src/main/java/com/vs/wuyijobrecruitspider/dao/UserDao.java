package com.vs.wuyijobrecruitspider.dao;

import com.vs.wuyijobrecruitspider.entity.JobInfo;
import com.vs.wuyijobrecruitspider.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Map;

public interface UserDao extends Repository<User, Integer> {
    List<User> findByUsernameIs(String username);

    @Query(value = "select job_id from user_starjob where user_id = ?", nativeQuery = true)
    List<Integer> findJobIdList(Integer userId);

    @Modifying
    @Query(value = "insert into user_starjob(job_id, user_id) values(?, ?)", nativeQuery = true)
    void saveUserStar(Integer jobId, Integer userId);

    @Modifying
    @Query(value = "delete from user_starjob where job_id = ? and user_id = ?", nativeQuery = true)
    void removeUserStar(Integer jobId, Integer userId);

    @Query(value = "select job_name jobName, url, company_name companyName, company_addr companyAddr, `time` from job_info j, user_starjob u where j.id = u.job_id and u.user_id = ?", nativeQuery = true)
    List<Map<String, Object>> findJobList(Integer userId);
}
