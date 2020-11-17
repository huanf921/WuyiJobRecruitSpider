package com.vs.wuyijobrecruitspider.dao;

import com.vs.wuyijobrecruitspider.entity.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobInfoDao extends JpaRepository<JobInfo, Long>, JpaSpecificationExecutor<JobInfo> {

    @Query(value = "SELECT COUNT(*) FROM (SELECT (salary_max + salary_min) / 2 salary FROM job_info) t WHERE salary BETWEEN ? AND ?", nativeQuery = true)
    Integer findJobsBySalary(Integer minSalary, Integer maxSalary);

    @Query(value = "SELECT COUNT(*) FROM (SELECT (salary_max + salary_min) / 2 salary FROM job_info) t WHERE salary > ?", nativeQuery = true)
    Integer findJobsBySalary(Integer minSalary);

    //@Query(value =
    //        "SELECT id, job_name, company_name, job_info, company_info, company_addr, `time`" +
    //        "FROM job_info" +
    //        "WHERE job_name LIKE CONCAT(\"%\",#{keyword},\"%\")" +
    //        "OR company_name LIKE CONCAT(\"%\",#{keyword},\"%\")" +
    //        "OR company_addr LIKE CONCAT(\"%\",#{keyword},\"%\")", nativeQuery = true)
    //List<JobInfo> findJobInfoByPage(String keyword);
}
