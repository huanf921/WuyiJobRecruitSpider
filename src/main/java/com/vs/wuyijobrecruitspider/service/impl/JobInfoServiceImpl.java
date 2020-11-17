package com.vs.wuyijobrecruitspider.service.impl;

import com.vs.wuyijobrecruitspider.dao.JobInfoDao;
import com.vs.wuyijobrecruitspider.entity.JobInfo;
import com.vs.wuyijobrecruitspider.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoDao jobInfoDao;

    /**
     * 保存工作信息
     * @param jobInfo
     */
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

    /**
     * 根据条件查询工作信息
     * @param jobInfo
     * @return
     */
    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        return jobInfoDao.findAll(Example.of(jobInfo));
    }

    /**
     * 根据 关键词 和 分页参数 查询职位信息
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<JobInfo> findJobInfoByPage(String keyword, Integer pageNum, Integer pageSize) {
        //查询条件存在这个对象中
        Specification<JobInfo> specification = new Specification<JobInfo>() {
            //重新Specification的toPredicate方法
            @Override
            public Predicate toPredicate(Root<JobInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                //我要模糊查询的字段是jobName
                Path jobName = root.get("jobName");
                Path companyName = root.get("companyName");
                Path companyAddr = root.get("companyAddr");
                //criteriaBuilder.like模糊查询，第一个参数是上一行的返回值，第二个参数是like('%xxx%')中，xxx的值
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(jobName, "%" + keyword + "%")));
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(companyName, "%" + keyword + "%")));
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(companyAddr, "%" + keyword + "%")));
                Predicate predicate = criteriaBuilder.or(
                        predicates.toArray(new Predicate[predicates.size()])
                );

                return predicate;
            }
        };

        //分页条件存在这个对象中
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);

        //进行查询操作，第一个参数是查询条件对象，第二个参数是分页对象
        Page<JobInfo> page = jobInfoDao.findAll(specification, pageRequest);

        //返回的数据都封装在了Page<JobInfo>对象中
        return page;
    }

    @Override
    public Integer getJobsBySalary(Integer minSalary, Integer maxSalary) {
        return jobInfoDao.findJobsBySalary(minSalary, maxSalary);
    }

    @Override
    public Integer getJobsBySalary(Integer minSalary) {
        return jobInfoDao.findJobsBySalary(minSalary);
    }
}
