package com.freedom.id.service.eight.api;


import com.freedom.id.service.eight.api.vo.UserVo;

public interface FristDubboService {

    public String sayHello(String name);

    public String findGuoguo(Long id);

    public UserVo findUser(Long id);

    public Integer deleteUser(Long id);
}
