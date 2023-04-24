package com.freedom.controller;

import com.free.common.web.vo.ResponseVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first")
public class FirstController {
    @RequestMapping("/test")
    public ResponseVo test(){

        return ResponseVo.success("果果你好");
    }
}
