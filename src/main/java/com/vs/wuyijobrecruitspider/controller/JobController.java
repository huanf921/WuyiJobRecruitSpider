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
import java.util.List;

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
    public String doLogout(HttpSession session) {
        //强制session失效
        session.invalidate();
        return "redirect:/job/login";
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
        System.out.println(pageInfo);
        return (pageInfo.getContent() == null || pageInfo.getContent().isEmpty()) ? null : pageInfo;
    }
}
