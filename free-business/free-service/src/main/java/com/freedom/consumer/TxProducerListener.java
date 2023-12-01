package com.freedom.consumer;

import com.alibaba.fastjson.JSON;
import com.freedom.model.User;
import com.freedom.model.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service
@RocketMQTransactionListener
public class TxProducerListener implements RocketMQLocalTransactionListener {

    @Autowired
    private UserMapper userMapper;




    /**
     * 每次推送消息会执行executeLocalTransaction方法，首先会发送半消息，到这里的时候是执行具体本地业务，
     * 执行成功后手动返回RocketMQLocalTransactionState.COMMIT状态，
     * 这里是保证本地事务执行成功，如果本地事务执行失败则可以返回ROLLBACK进行消息回滚。 此时消息只是被保存到broker，并没有发送到topic中，broker会根据本地返回的状态来决定消息的处理方式。
     * @param msg
     * @param arg
     * @return
     */
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("开始执行本地事务");
        User user = JSON.parseObject((byte[]) msg.getPayload(),User.class);

        int row = userMapper.insertSelective(user);

        if(new Random().nextInt(10) % 2 == 0){
            return RocketMQLocalTransactionState.UNKNOWN;
        }


        log.info("本地事务提交");
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("开始执行回查");
        String key = msg.getHeaders().get("key").toString();

        User select = userMapper.selectByPrimaryKey(Long.valueOf(key));
        if (select == null) {
            log.info("回滚半消息");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        log.info("提交半消息");
        return RocketMQLocalTransactionState.COMMIT;
    }
}

