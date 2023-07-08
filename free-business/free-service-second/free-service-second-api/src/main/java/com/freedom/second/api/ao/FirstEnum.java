package com.freedom.second.api.ao;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.free.common.model.IDict;

@JsonFormat(shape = JsonFormat.Shape.ANY)
@JSONType(serializeEnumAsJavaBean = true)
public enum FirstEnum implements IDict<Integer> {
    ONE(1,"你好"),
    TWO(2,"不好");


    //@JSONField(serialize = false)
    Integer code;
    String  text;

    /*FirstEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }*/

    FirstEnum(Integer code, String text) {
        init(code, text);
    }
}
