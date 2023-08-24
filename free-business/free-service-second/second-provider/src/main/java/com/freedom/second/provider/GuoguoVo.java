package com.freedom.second.provider;

import com.alibaba.fastjson.JSON;
import com.freedom.second.controller.SignType;
import lombok.Data;

@Data
public class GuoguoVo {

    SignType signType;

    public static void main(String[] args){
        GuoguoVo guoguoVo = new GuoguoVo();
        guoguoVo.setSignType(SignType.ADD);
        System.out.println(JSON.toJSONString(guoguoVo));
    }
}
