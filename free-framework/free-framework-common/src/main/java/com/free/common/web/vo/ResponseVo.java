package com.free.common.web.vo;

public class ResponseVo <T>{
    private Integer code;
    private String message;

    private static Integer errorCode = -1;
    private static Integer successCode =1;

    private T data;

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
}
