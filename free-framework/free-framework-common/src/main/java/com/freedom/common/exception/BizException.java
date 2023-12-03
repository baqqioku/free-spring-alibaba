package com.freedom.common.exception;

public class BizException extends RuntimeException{

    private final String code;
    private final String msg;

    public BizException(IResultCode iResultCode) {
        super(iResultCode.getCode() + iResultCode.getMessage());
        this.code = iResultCode.getCode();
        this.msg = iResultCode.getMessage();
    }

    public BizException(String code, String message) {
        super(code + message);
        this.code = code;
        this.msg = message;
    }

    public BizException(String message) {
        super(message);
        this.code = BaseResultCodeEnum.BIZ_ERROR.getCode();
        this.msg = message;
    }


    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}
