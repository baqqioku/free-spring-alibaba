package com.freedom.second.controller;

import com.freedom.framework.web.swagger.ApiModelPropertyEnum;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel
public class GuoguoAo implements Serializable {
    private String name;

    @ApiModelPropertyEnum(dictEnum = SignType.class)
    SignType signType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SignType getSignType() {
        return signType;
    }

    public void setSignType(SignType signType) {
        this.signType = signType;
    }
}
