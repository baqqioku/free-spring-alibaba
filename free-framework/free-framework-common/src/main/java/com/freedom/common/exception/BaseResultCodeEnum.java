package com.freedom.common.exception;


import com.freedom.common.model.IDict;

/**
 * 通用结果编码枚举
 *
 * @author liao, R
 */

public enum BaseResultCodeEnum implements IResultCode, IDict<String> {

    //
    SUCCESS("SUCCESS", "操作成功"),

    BIZ_ERROR("BIZ_ERROR", "业务处理异常"),

    SYSTEM_ERROR("SYSTEM_ERROR", "❌系统异常"),

    INTERFACE_SYSTEM_ERROR("INTERFACE_SYSTEM_ERROR", "外部接口调用异常"),

    CONNECT_TIME_OUT("CONNECT_TIME_OUT", "系统超时"),

    NULL_ARGUMENT("NULL_ARGUMENT", "参数为空"),

    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "参数不合法"),

    ILLEGAL_REQUEST("ILLEGAL_REQUEST", "非法请求"),

    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "请求方法不允许"),

    ILLEGAL_CONFIGURATION("ILLEGAL_CONFIGURATION", "配置不合法"),

    ILLEGAL_STATE("ILLEGAL_STATE", "状态不合法"),

    ENUM_CODE_ERROR("ENUM_CODE_ERROR", "错误的枚举编码"),

    LOGIC_ERROR("LOGIC_ERROR", "逻辑错误"),

    CONCURRENT_ERROR("CONCURRENT_ERROR", "并发异常"),

    ILLEGAL_OPERATION("ILLEGAL_OPERATION", "非法操作"),

    REPETITIVE_OPERATION("REPETITIVE_OPERATION", "重复操作"),

    NO_OPERATE_PERMISSION("NO_OPERATE_PERMISSION", "无操作权限"),

    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "资源不存在"),

    RESOURCE_ALREADY_EXIST("RESOURCE_ALREADY_EXIST", "资源已存在"),

    TYPE_UN_MATCH("TYPE_UN_MATCH", "类型不匹配"),

    FILE_NOT_EXIST("FILE_NOT_EXIST", "文件不存在"),

    LIMIT_BLOCK("LIMIT_BLOCK", "请求限流阻断"),

    TOKEN_FAIL("TOKEN_FAIL", "token校验失败"),

    TOKEN_EXPIRE("TOKEN_EXPIRE", "token过期"),

    REQUEST_EXCEPTION("REQUEST_EXCEPTION", "请求异常"),

    BLOCK_EXCEPTION("BLOCK_EXCEPTION", "接口限流降级"),

    ;


    BaseResultCodeEnum(String code, String message) {
        init(code, message);
    }


    @Override
    public String getCode() {
        return IDict.super.getCode();
    }

    @Override
    public String getMessage() {
        return IDict.super.getText();
    }
}
