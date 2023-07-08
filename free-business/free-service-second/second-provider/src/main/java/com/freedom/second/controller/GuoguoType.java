package com.freedom.second.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.free.common.model.IDict;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GuoguoType  {

    ADD(1,"补款"),
    SUB(-1,"扣款");

    GuoguoType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    //@JsonProperty(value = "signType", access = JsonProperty.Access.WRITE_ONLY)
    //@JsonValue
    private int code;
    private String desc;

    //@JsonCreator
    public static GuoguoType parse(int code) {
        GuoguoType[] values = GuoguoType.values();
        for (GuoguoType value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
    //@JsonValue
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

    public static void main(String[] args){
        System.out.println(GuoguoType.class.getSimpleName());
    }
}
