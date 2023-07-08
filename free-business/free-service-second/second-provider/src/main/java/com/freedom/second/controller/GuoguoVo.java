package com.freedom.second.controller;

import java.io.Serializable;


public class GuoguoVo implements Serializable {
    private String name;

    GuoguoType guoguoType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GuoguoType getGuoguoType() {
        return guoguoType;
    }

    public void setGuoguoType(GuoguoType guoguoType) {
        this.guoguoType = guoguoType;
    }
}
