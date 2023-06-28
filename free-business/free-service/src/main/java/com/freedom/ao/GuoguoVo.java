package com.freedom.ao;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

public class GuoguoVo implements Serializable {
    private String name;

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
