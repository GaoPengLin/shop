package com.gao.shop.base;

/**
 * @author gaopenglin
 * @description: 服务接口类
 * @date 2023/02/15
 */
public interface BaseErrorInfoInterface {

    /**
     * 错误码
     *
     * @return
     */
    String getResultCode();

    /**
     * 错误描述
     *
     * @return
     */
    String getResultMsg();

}
