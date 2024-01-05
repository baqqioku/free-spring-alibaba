package com.freedom.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Data
public class UserLoginInfo implements Serializable {

    /**
     * 用户id
     */
    private Long id;

}
