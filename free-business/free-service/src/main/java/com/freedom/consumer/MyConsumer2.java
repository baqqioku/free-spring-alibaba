/*
package com.freedom.consumer;

import com.alibaba.fastjson.JSON;
import com.freedom.model.AccountTbl;
import com.freedom.model.mapper.AccountTblMapper;
import com.freedom.model.mapper.AccountTblNextMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@RocketMQMessageListener(topic = "guoguo", consumerGroup = "guoguotest")
@Slf4j
public class MyConsumer2 implements RocketMQListener<String> {

    @Autowired
    AccountTblNextMapper accountTblMapper;

    @Override
    public void onMessage(String message) {
        //System.out.println(message);
        List<AccountTbl> tblList = JSON.parseArray(message, AccountTbl.class);
        for(AccountTbl accountTbl : tblList){
            AccountTbl accountTbl1 = accountTblMapper.selectByPrimaryKey(accountTbl.getId());
            log.info(JSON.toJSONString(accountTbl1));
        }

        //log.info(JSON.toJSONString(accountTbl1));
    }
}
*/
