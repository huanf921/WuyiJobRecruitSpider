package com.vs.wuyijobrecruitspider.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.vs.wuyijobrecruitspider.entity.JobInfo;
import com.vs.wuyijobrecruitspider.entity.User;
import com.vs.wuyijobrecruitspider.service.JobInfoService;
import com.vs.wuyijobrecruitspider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private UserService userService;
    @Autowired
    private JobInfoService jobService;

    @RequestMapping("/login")
    public String login(Model model,
                        @RequestParam(required = false) String username,
                        @RequestParam(required = false) String password,
                        HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "index";
        }
        User userDB = userService.getUser(username);
        if (userDB == null) {
            model.addAttribute("userNotExist", "请检查账号是否正确");
            return "login";
        } else {
            // 使用 hutool 加密工具，采用摘要加密算法 MD5 加密密码
            String pwdVO = DigestUtil.md5Hex(password);
            if (pwdVO.equals(userDB.getPassword())) {
                model.addAttribute("user", userDB);
                session.setAttribute("user", userDB);
                return "index";
            } else {
                model.addAttribute("passwordError", "请检查密码是否正确");
                return "login";
            }
        }
    }

    @RequestMapping("/logout")
    public String doLogout(Model model, HttpSession session) {
        //强制session失效
        session.invalidate();
        model.addAttribute("userNotExist", null);
        model.addAttribute("passwordError", null);
        return "redirect:/user/login";
    }

    @RequestMapping("/showpage")
    @ResponseBody
    public Page<JobInfo> getPage(
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        //System.out.println(pageNum + " " + pageSize);
        //Integer pageNum = Integer.valueOf(pageNums);
        //Integer pageSize = Integer.valueOf(pageSizes);
        Page<JobInfo> pageInfo = jobService.findJobInfoByPage(keyword, pageNum, pageSize);
        //System.out.println(pageInfo);
        return (pageInfo.getContent() == null || pageInfo.getContent().isEmpty()) ? null : pageInfo;
    }

    @RequestMapping("/getstar")
    @ResponseBody
    public List<Integer> getStarJob(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Integer userId = user.getId();
        List<Integer> jobIdList = userService.getJobIdListByUserId(userId);

        return jobIdList;
    }

    @RequestMapping("/getfullstar")
    @ResponseBody
    public List<Map<String, Object>> getFullStarJob(Integer userId) {
        // jpa无法自动映射部分字段，只能映射全部
        List<Map<String, Object>> jobList = userService.getJobListByUserId(userId);

        return jobList;
    }

    @RequestMapping("/getjobaddr")
    @ResponseBody
    public List<Map<String, Object>> getJobAddr() {
        List<Map<String, Object>> res = new ArrayList<>();
        String[] addresses = {"广州", "深圳", "上海", "武汉", "北京", "宁波", "成都", "苏州", "南京", "杭州"};

        // 分别获取主要地区的岗位数量，返回前端
        for (String address : addresses) {
            Integer jobCount = userService.getJobsByAddr(address);
            HashMap<String, Object> addrJobsMap = new HashMap<>();
            addrJobsMap.put("city", address);
            addrJobsMap.put("count", jobCount);
            res.add(addrJobsMap);
        }

        return res;
    }

    @RequestMapping("/getjobsalary")
    @ResponseBody
    public List<Map<String, Object>> getJobSalary() {
        List<Map<String, Object>> res = new ArrayList<>();
        int[] salary = {5 * 1000 * 12, 10 * 1000 * 12, 20 * 1000 * 12, 30 * 1000 * 12};
        Integer minSalary = 0;
        Integer maxSalary = 0;
        // 分别获取对应薪资范围的岗位数量，返回前端
        for (int i = 0; i < salary.length; i++) {
            String salaryRange = "";
            Integer jobCount = 0;
            if (i == 0) {
                salaryRange = salary[i] + "以下";
                minSalary = 0;
                maxSalary = salary[i];
                jobCount = jobService.getJobsBySalary(minSalary, maxSalary);
            } else if (i == salary.length - 1) {
                salaryRange = salary[i] + "以上";
                jobCount = jobService.getJobsBySalary(salary[i]);
            } else {
                salaryRange = salary[i - 1] + "~" + salary[i];
                minSalary = salary[i - 1];
                maxSalary = salary[i];
                jobCount = jobService.getJobsBySalary(minSalary, maxSalary);
            }

            Map<String, Object> salaryJobsMap = new HashMap<>();
            salaryJobsMap.put("salary", salaryRange);
            salaryJobsMap.put("count", jobCount);
            res.add(salaryJobsMap);
        }

        return res;
    }

    @RequestMapping("/savestar")
    @ResponseBody
    public void saveStarJob(Integer jobId, Integer userId) {
        userService.saveUserStar(jobId, userId);
    }

    @RequestMapping("/removestar")
    @ResponseBody
    public void removeStarJob(Integer jobId, Integer userId) {
        userService.removeUserStar(jobId, userId);
    }
}
