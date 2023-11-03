package com.freedom.framework.dubbo;

import com.freedom.common.exception.BaseResultCodeEnum;
import com.freedom.common.web.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DubboExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(RpcException.class)
    public ResponseVo<Void> rpcException(RpcException rpcException) {
        log.error("dubbo调用异常", rpcException);
        return ResponseVo.error(BaseResultCodeEnum.SYSTEM_ERROR.getMessage()+"dubbo调用异常:" + rpcException.getMessage());
    }
}
