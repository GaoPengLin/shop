package com.gao.shop.mapper;

import com.gao.shop.entity.admin.user.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户映射器
 *
 * @author gaopenglin
 * @date 2023/02/16
 */
@Mapper
public interface SystemUserMapper {

    /**
     * 根据用户姓名查询用户
     *
     * @param uName 用户登录账号
     * @return {@link SystemUser}
     */
    SystemUser findByUserName(@Param("uName") String uName);

}
