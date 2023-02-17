package com.gao.shop.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSONObject;
import com.gao.shop.entity.admin.user.SystemUser;
import com.gao.shop.enumclass.ExceptionEnum;
import com.gao.shop.mapper.SystemUserMapper;
import com.gao.shop.org.mindrot.jbcrypt.BCrypt;
import com.gao.shop.result.ResultResponse;
import com.gao.shop.service.SystemUserService;
import com.gao.shop.util.JwtUtil;
import com.gao.shop.vo.AdminUserVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 系统用户服务impl
 *
 * @author gaopenglin
 * @date 2023/02/16
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {

    @Resource
    private SystemUserMapper userMapper;

    /**
     * 管理员用户登录
     *
     * @param adminUserVO 用户VO
     * @return boolean
     */
    @Override
    public ResultResponse adminUserLogin(AdminUserVO adminUserVO) {
        //根据账号去数据库查询用户信息
        SystemUser systemUser = userMapper.findByUserName(adminUserVO.getUserName());
        if (ObjectUtils.isEmpty(systemUser)) {
            throw new RuntimeException("用户名不存在");
        }
        //进行用户密码校验（明文密码，数据库秘文）
        boolean result = BCrypt.checkpw(adminUserVO.getPwd(), systemUser.getPwd());
        if (result) {
            JSONObject data = new JSONObject();
            //获取Token
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), adminUserVO.getUserName(), null);
            data.put("username", adminUserVO.getUserName());
            data.put("token", token);
            return ResultResponse.success(data);
        } else {
            return ResultResponse.error(ExceptionEnum.USER_OR_PWD_ERROR);
        }
    }
}
