package com.freedom.ao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SignType {

    ADD(1,"补款"),
    SUB(-1,"扣款");

    SignType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    //@JsonProperty(value = "signType", access = JsonProperty.Access.WRITE_ONLY)
    private int code;
    private String desc;

    //@JsonCreator
    public static SignType parse(int code) {
        SignType[] values = SignType.values();
        for (SignType value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
