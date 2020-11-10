package com.vs.wuyijobrecruitspider.dao;

import com.vs.wuyijobrecruitspider.entity.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobInfoDao extends JpaRepository<JobInfo, Long> {
}
