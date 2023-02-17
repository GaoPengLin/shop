package com.gao.shop.service;

import com.gao.shop.result.ResultResponse;
import com.gao.shop.vo.AdminUserVO;

/**
 * 系统管理用户业务类
 *
 * @author gaopenglin
 * @date 2023/02/16
 */
public interface SystemUserService {

    /**
     * 管理员用户登录
     *
     * @param adminUserVO 用户VO
     * @return boolean
     */
    ResultResponse adminUserLogin(AdminUserVO adminUserVO);

}
