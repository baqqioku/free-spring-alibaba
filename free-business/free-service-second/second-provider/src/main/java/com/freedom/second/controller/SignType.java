package com.freedom.second.controller;

import com.freedom.common.model.IDict;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SignType  implements IDict<Integer> {

    ADD(1,"补款"),
    SUB(-1,"扣款");

    SignType(int code, String desc) {
        init(code,desc);
    }

    //@JsonProperty(value = "signType", access = JsonProperty.Access.WRITE_ONLY)
    /*@JsonValue
    private int code;
    private String desc;

    @JsonCreator
    public static SignType parse(int code) {
        SignType[] values = SignType.values();
        for (SignType value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
*/
    /*@JsonValue
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
    }*/
}
