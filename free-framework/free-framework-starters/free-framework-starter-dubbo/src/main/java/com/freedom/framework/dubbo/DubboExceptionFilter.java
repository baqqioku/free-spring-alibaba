package com.freedom.framework.dubbo;

import com.freedom.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER},order = -1000)
public class DubboExceptionFilter implements Filter,Filter.Listener {

    private String errorInfo(Invoker<?> invoker,Invocation invocation,Throwable exception){
        return "got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHostName()+
                ". service: " + invoker.getInterface().getName() +",method: "+ invocation.getMethodName()+" " +
                ", exception: " + exception.getClass().getName() + ": " + exception.getMessage() +
                ", args: "+ Arrays.toString(invocation.getArguments());
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
               throw new RpcException(errorInfo);
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

                if (exception instanceof BizException) {
                    return;
                }

                // directly throw if it's checked exception
                if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
                    return;
                }
                // directly throw if the exception appears in the signature
                Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                Class<?>[] exceptionClasses = method.getExceptionTypes();
                for (Class<?> exceptionClass : exceptionClasses) {
                    if (exception.getClass().equals(exceptionClass)) {
                        return;
                    }
                }

                String errorInfo = errorInfo(invoker, invocation, exception);

                // for the exception not found in method's signature, print ERROR message in server's log.
                log.error(errorInfo, exception);

                // directly throw if exception class and interface class are in the same jar file.
                String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
                    return;
                }
                // directly throw if it's JDK exception
                String className = exception.getClass().getName();
                if (className.startsWith("java.") || className.startsWith("javax.")) {
                    return;
                }
                // directly throw if it's dubbo exception
                if (exception instanceof RpcException) {
                    return;
                }

                // otherwise, wrap with RuntimeException and throw back to the client
                appResponse.setException(new RuntimeException(StringUtils.toString(exception)));
            } catch (Throwable e) {
                log.warn("Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
        log.error("DubboExceptionFilter error", t);
    }
}
