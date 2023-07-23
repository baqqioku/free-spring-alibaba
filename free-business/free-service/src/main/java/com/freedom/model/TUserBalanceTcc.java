package com.freedom.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_user
 * @author 
 */
@Data
public class TUserBalanceTcc implements Serializable {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private Date deleteTime;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 登录密码
     */
    private String loginPwd;

    /**
     * 密码盐
     */
    private String pwdSalt;

    /**
     * 余额, 单位分
     */
    private Long balance;

    private static final long serialVersionUID = 1L;
}