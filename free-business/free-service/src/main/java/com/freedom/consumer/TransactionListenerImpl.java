package com.freedom.consumer;

import com.alibaba.fastjson.JSON;
import com.freedom.model.AccountTbl;
import com.freedom.model.mapper.AccountTblMapper;
import com.freedom.id.service.FristService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

@Component
@Slf4j
public class TransactionListenerImpl implements TransactionListener {

    @Resource
    private AccountTblMapper accountTblMapper;

    @Resource
    private FristService fristService;

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        //Order order = JSON.parseObject(msg.getBody(), Order.class);
        //updateOrder(order);

        AccountTbl accountTbl = JSON.parseObject(msg.getBody(),AccountTbl.class);
        // 执行本地事务
        boolean success = false;
        try {
            success = fristService.test2(accountTbl);
        }catch (Exception e){
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        Random romdom = new Random();
        if(romdom.nextInt(10) %2==0){
           return LocalTransactionState.UNKNOW;
        }
        // 返回事务状态
        return success ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
    }


    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        //boolean success = checkLocalTransaction();
        AccountTbl accountTbl = JSON.parseObject(msg.getBody(),AccountTbl.class);
        log.info("回查第一个");
        boolean success = accountTblMapper.selectByPrimaryKey(accountTbl.getId()) != null;
        /*try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        if(success){
            log.info("提交半消息");
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        log.info("回滚半消息");
        return success ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
    }

   private boolean executeLocalTransaction() {
        // 执行本地事务
        return true;
    }

    private boolean checkLocalTransaction() {
        // 判断本地事务是否执行成功
        return true;
    }

    private void updateOrder(Order order) {
        // 更新订单信息
    }
}
