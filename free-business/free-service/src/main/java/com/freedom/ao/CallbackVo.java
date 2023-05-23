package com.freedom.ao;

import java.io.Serializable;

public class CallbackVo implements Serializable {
    private String sb;

    private Integer type;

    public String getSb() {
        return sb;
    }

    public void setSb(String sb) {
        this.sb = sb;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
