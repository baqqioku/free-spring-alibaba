package com.freedom.common.web.vo;

import com.freedom.common.exception.IResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

@Setter
@Getter
@ToString
public class ResponseVo <T>{

    @ApiModelProperty("结果码")
    private Integer code;
    @ApiModelProperty("消息")
    private String message;
    @ApiModelProperty("链路id")
    private String traceId;
    @ApiModelProperty("返回数据内容")
    private T data;


    private static Integer errorCode = -1;
    private static Integer successCode =1;

    public ResponseVo() {
    }

    //private ResponseVo<T> errorReponse = new ResponseVo<>(errorCode,"失败");


    public ResponseVo(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseVo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseVo success(T data){
        return  new ResponseVo<T>(successCode,"成功",data);
    }

    public static <T> ResponseVo error(T data){
        return  new ResponseVo<T>(errorCode,"失败",data);
    }

    public static <T> ResponseVo error(String message){
        return  new ResponseVo<T>(errorCode,message);
    }

    /*public static ResponseVo<Void> fail(IResultCode resultCode, String detailErrorMsg) {
        return new ResponseVo<Void>(resultCode.getCode(), resultCode.getMessage() + "：" + detailErrorMsg);
    }*/

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Trace
    public void setTraceId() {
        this.traceId = TraceContext.traceId();
    }
}
