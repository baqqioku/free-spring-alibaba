package com.freedom.framework.dubbo;

import com.freedom.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER},order = -1000)
public class DubboExceptionFilter implements Filter,Filter.Listener {

    private String errorInfo(Invoker<?> invoker,Invocation invocation,Throwable exception){
        return "got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHostName()+
                ". service: " + invoker.getInterface().getName() +",method: "+ invocation.getMethodName()+" , args: "+ Arrays.toString(invocation.getArguments());
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
       Result invoke;
       try {
           invoke = invoker.invoke(invocation);
           if (invoker != null && invoke.getException() != null) {
               if (invoke.getException().getClass() == BizException.class) {
                   return invoke;
               }
               Throwable exception = invoke.getException();
               log.error("dubbo 接口异常", invoke.getException());
               String errorInfo = errorInfo(invoker, invocation, exception);
               throw new RpcException(exception);
           }
       }catch (RpcException | BizException e) {
           throw e;
       } catch (Throwable e) {
           String errorInfo = errorInfo(invoker, invocation, e);
           log.error(errorInfo);
           throw e;
       }
       return invoke;


    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if(appResponse.hasException() && GenericService.class != invoker.getInterface() ){
            try{
                Throwable exception = appResponse.getException();
                if(exception instanceof BizException){
                   return;

                }

                // directly throw if it's checked exception
                if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
                    return;
                }

                Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());

            }catch (Exception e){

            }

        }
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {

    }
}
