package com.vs.wuyijobrecruitspider.service;

import com.vs.wuyijobrecruitspider.entity.JobInfo;

import java.util.List;

public interface JobInfoService {
    /**
     * 保存工作信息
     * @param jobInfo
     */
    void save(JobInfo jobInfo);

    /**
     * 根据条件查询工作信息
     * @param jobInfo
     * @return
     */
    List<JobInfo> findJobInfo(JobInfo jobInfo);
}
