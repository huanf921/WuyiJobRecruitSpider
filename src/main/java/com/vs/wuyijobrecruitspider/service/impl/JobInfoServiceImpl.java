package com.vs.wuyijobrecruitspider.service.impl;

import com.vs.wuyijobrecruitspider.dao.JobInfoDao;
import com.vs.wuyijobrecruitspider.entity.JobInfo;
import com.vs.wuyijobrecruitspider.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoDao jobInfoDao;

    @Override
    public void save(JobInfo jobInfo) {
        JobInfo query = new JobInfo();
        query.setUrl(jobInfo.getUrl());
        query.setTime(jobInfo.getTime());

        List<JobInfo> jobInfoListPO = findJobInfo(query);

        // 判断查询结果是否为空
        if (jobInfoListPO.size() == 0) {
            // 如果查询结果为空, 表示招聘信息数据不存在, 或者已经更新了, 需要增或者更新数据库
            jobInfoDao.saveAndFlush(jobInfo);
        }
    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        return jobInfoDao.findAll(Example.of(jobInfo));
    }
}
