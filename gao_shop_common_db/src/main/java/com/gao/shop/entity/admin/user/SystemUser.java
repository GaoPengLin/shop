package com.gao.shop.entity.admin.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 系统用户
 *
 * @author gaopenglin
 * @date 2023/02/16
 */
@Data
public class SystemUser implements Serializable {

    @Serial
    private static final long serialVersionUID = -2808922793033438534L;
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户手机号
     */
    private String phoneNumber;
    /**
     * 登录密码
     */
    private String pwd;
    /**
     * 用户性别（字典表：sex）
     */
    private Integer sex;
    /**
     * 用户头像地址
     */
    private String avatar;
    /**
     * 用户状态：1:正常，2:禁用，3:注销
     */
    private Integer userStatus;
    /**
     * 创建时间
     */
    private Date createDate;

}
