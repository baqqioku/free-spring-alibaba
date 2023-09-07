package com.freedom.service;

import com.alibaba.fastjson.JSON;
import com.freedom.config.FirstConfig;
import com.freedom.framework.mq.config.WsRocketMQTemplate;
import com.freedom.model.AccountTbl;
import com.freedom.model.mapper.AccountTblNextMapper;
import com.freedom.second.api.SecondApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class FristService {

    @Autowired
    private FirstConfig firstConfig;

    @Autowired
    private AccountTblNextMapper accountTblMapper;


    @Autowired
    SecondApi secondApi;

    @PostConstruct
    public void test() {
        System.out.println(firstConfig.toString());
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Autowired
    private WsRocketMQTemplate rocketMQTemplate;

    //@GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void test1(String name) {

        //String xid = RootContext.getXID();
        //TransactionManager manager = TransactionManagerHolder.get();
        List<AccountTbl> list = new ArrayList<>();
        for (int i = 10; i < 20; i++) {
            AccountTbl accountTbl = new AccountTbl();
            accountTbl.setId(i);
            accountTbl.setMoney(i);
            accountTbl.setUserId(i+"");
            list.add(accountTbl)   ;

            //accountTblMapper.updateByPrimaryKeySelective(accountTbl);
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
           /* executor.submit(new Runnable() {
                @Override
                public void run() {

                    AccountTbl accountTbl1 = accountTblMapper.selectByPrimaryKey(2);
                    log.info(JSON.toJSONString(accountTbl1));

                }
            });*/
            /*TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

                @Override
                public void afterCommit() {
                    try {
                        // 在事务提交成功后发送消息到MQ
                        rocketMQTemplate.syncSend("guoguo", "2");
                    } catch (Exception e) {
                        // 发送消息到MQ时出现异常
                        // 标记当前事务为回滚状态
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        throw new RuntimeException("发送消息到MQ失败", e);
                    }
                }
            });*/
            //rocketMQTemplate.syncSend("guoguo", "2");

        }

        accountTblMapper.batchInsert(list);

        TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
        Object savepoint = transactionStatus.createSavepoint();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                try {
                    // 在事务提交成功后发送消息到MQ
                    //System.out.print(1/0);
                    rocketMQTemplate.syncSend("guoguo", JSON.toJSONString(list));
                    throw new RuntimeException("111");
                } catch (Exception e) {
                    // 发送消息到MQ时出现异常
                    // 标记当前事务为回滚状态
                    transactionStatus.rollbackToSavepoint(savepoint);
                    throw new RuntimeException("发送消息到MQ失败", e);
                }
            }
        });
    }


}
