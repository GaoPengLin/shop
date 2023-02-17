package com.gao.shop.handler;

import com.gao.shop.enumclass.ExceptionEnum;
import com.gao.shop.exception.BizException;
import com.gao.shop.result.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author gaopenglin
 * @date 2023/02/15
 */
@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResultResponse bizExceptionHandler(BizException e) {
        log.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultResponse.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是：", e);
        return ResultResponse.error(ExceptionEnum.BODY_NOT_MATCH);
    }

    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是:", e);
        return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ArithmeticException.class)
    @ResponseBody
    public ResultResponse calculateError(Exception e) {
        log.error("数学运算错误，错误原因：", e);
        return ResultResponse.error(ExceptionEnum.CALCULATE_ERROR);
    }

}
