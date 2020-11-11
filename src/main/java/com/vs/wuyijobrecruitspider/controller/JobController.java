package com.vs.wuyijobrecruitspider.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.vs.wuyijobrecruitspider.entity.User;
import com.vs.wuyijobrecruitspider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(Model model, String username, String password) {
        User userDB = userService.getUser(username);
        if (userDB == null) {
            model.addAttribute("userNotExist", "请检查账号是否正确");
            return "login";
        } else {
            // 使用 hutool 加密工具，采用摘要加密算法 MD5 加密密码
            String pwdVO = DigestUtil.md5Hex(password);
            if (pwdVO.equals(userDB.getPassword())) {
                model.addAttribute("user", userDB);
                return "index";
            } else {
                model.addAttribute("passwordError", "请检查密码是否正确");
                return "login";
            }
        }
    }

}
