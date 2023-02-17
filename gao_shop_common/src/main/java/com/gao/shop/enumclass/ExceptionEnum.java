package com.gao.shop.enumclass;

import com.gao.shop.base.BaseErrorInfoInterface;

/**
 * 错误枚举类
 *
 * @author gaopenglin
 * @date 2023/02/15
 */
public enum ExceptionEnum implements BaseErrorInfoInterface {
    // 数据操作错误定义
    SUCCESS("2000", "成功!"),
    BODY_NOT_MATCH("4000", "请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("4001", "请求的数字签名不匹配!"),
    NOT_FOUND("4004", "未找到该资源!"),
    INTERNAL_SERVER_ERROR("5000", "服务器内部错误!"),
    SERVER_BUSY("5003", "服务器正忙，请稍后再试!"),
    PARAMS_NOT_CONVERT("4002", "类型转换不对!"),
    USER_OR_PWD_ERROR("5005", "账号密码错误"),
    CALCULATE_ERROR("5004", "数学运算错误!"),
    TOKEN_IS_NULL("5005", "Token为空"),
    TOKEN_ANALYSIS_ERROR("5006", "Token解析失败");


    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
