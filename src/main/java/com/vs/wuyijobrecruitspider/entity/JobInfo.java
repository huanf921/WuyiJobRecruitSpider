package com.vs.wuyijobrecruitspider.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "job_info")
public class JobInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_addr")
    private String companyAddr;

    @Column(name = "company_info")
    private String companyInfo;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_addr")
    private String jobAddr;

    @Column(name = "job_info")
    private String jobInfo;

    @Column(name = "salary_min")
    private Integer salaryMin;

    @Column(name = "salary_max")
    private Integer salaryMax;

    @Column(name = "url")
    private String url;

    @Column(name = "time")
    private String time;

}
