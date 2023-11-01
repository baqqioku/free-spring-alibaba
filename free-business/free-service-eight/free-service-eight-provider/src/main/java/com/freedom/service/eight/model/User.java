package com.freedom.service.eight.model;

import java.time.LocalDateTime;

public class User {
    /**
    * 自增ID
    */
    private Long id;

    /**
    * 用户ID
    */
    private String userid;

    /**
    * 用户头像
    */
    private String userhead;

    /**
    * 创建时间
    */
    private LocalDateTime createtime;

    /**
    * 更新时间
    */
    private LocalDateTime updatetime;

    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserhead() {
        return userhead;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
        this.createtime = createtime;
    }

    public LocalDateTime getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(LocalDateTime updatetime) {
        this.updatetime = updatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}