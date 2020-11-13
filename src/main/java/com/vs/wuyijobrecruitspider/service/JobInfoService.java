package com.vs.wuyijobrecruitspider.service;

import com.vs.wuyijobrecruitspider.entity.JobInfo;
import org.springframework.data.domain.Page;

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

    /**
     * 根据 关键词 和 分页参数 查询职位信息
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<JobInfo> findJobInfoByPage(String keyword, Integer pageNum, Integer pageSize);
}
