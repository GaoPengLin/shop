package com.gao.shop.controller;

import com.gao.shop.org.mindrot.jbcrypt.BCrypt;
import com.gao.shop.result.ResultResponse;
import com.gao.shop.service.SystemUserService;
import com.gao.shop.vo.AdminUserVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理用户身份验证conteoller
 *
 * @author gaopenglin
 * @date 2023/02/07
 */
@RestController
@RequestMapping("/admin")
public class AdminUserAuthController {

    @Resource
    private SystemUserService userService;

    public static void main(String[] args) {
        //这个是盐 29个字符，随机生 成
        String str = BCrypt.gensalt();
        //根据 盐对密码进行加密
        String password = BCrypt.hashpw("123456", str);
        //加密后的字符串前29位就是盐
        System.out.println(password);
        boolean checkpw = BCrypt.checkpw("123456", password);
        System.out.println(checkpw);
    }

    @PostMapping("/login")
    public ResultResponse getTestUser(@RequestBody AdminUserVO adminUserVO) {
        return userService.adminUserLogin(adminUserVO);
    }

    @PostMapping("/addUser")
    public ResultResponse addUser() {
        return ResultResponse.success();
    }


}
