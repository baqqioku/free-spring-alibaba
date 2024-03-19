package com.free.service.ten.controller;


import com.freedom.common.web.vo.ResponseVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenTest")
public class TenTestController {

    @RequestMapping("/test")
    public ResponseVo test(){
        return new ResponseVo();
    }
}
