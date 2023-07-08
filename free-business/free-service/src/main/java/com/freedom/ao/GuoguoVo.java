package com.freedom.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data

public class GuoguoVo implements Serializable {
    private String name;

    SignType signType;


}
