package com.freedom.producer;

import com.alibaba.fastjson.JSON;
import com.freedom.consumer.TransactionListenerImpl;
import com.freedom.model.AccountTbl;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.*;

//@Component
public class TransactionProducer  implements InitializingBean {

    private  TransactionMQProducer producer = new TransactionMQProducer("guoguo-trans");

    @Resource
    private TransactionListenerImpl transactionListener;

    @Override
    public void afterPropertiesSet() throws Exception {
        //producer = new TransactionMQProducer("guoguo-trans");

        producer.setNamesrvAddr("rocketmq01.dev02.bmpl.ws.srv:9876");
       // producer.setNamesrvAddr(" 172.16.5.88:9876");



        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        producer.setExecutorService(executorService);
        //设置回调检查监听器
        producer.setTransactionListener(transactionListener);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public  TransactionMQProducer getInstance(){
        if (producer != null){
            return producer;
        }
        return null;
    }

    public void test() throws UnsupportedEncodingException, MQClientException {
        for(int i=0;i<10;i++){
            String businessNo = UUID.randomUUID().toString();

            AccountTbl accountTbl = new AccountTbl();
            //accountTbl.setId(i);
            accountTbl.setMoney(i);
            accountTbl.setUserId(i+"");
            /*Message msg = new Message("TransanctionMessage", "tag", businessNo,
                    JSON.toJSONString(transferRecord).getBytes(RemotingHelper.DEFAULT_CHARSET));*/
            //SendResult sendResult = producer.sendMessageInTransaction(msg, null);
            //org.apache.rocketmq.common.message.Message message = new org.apache.rocketmq.common.message.Message("guoguo-transaction","tag", businessNo,JSON.toJSONString(accountTbl).getBytes(RemotingHelper.DEFAULT_CHARSET));
            org.apache.rocketmq.common.message.Message msg = new Message("guoguo-transaction", "g", businessNo,
                    JSON.toJSONString(accountTbl).getBytes(RemotingHelper.DEFAULT_CHARSET));

            //SendResult sendResult = TransactionProducer.getInstance().sendMessageInTransaction(message, null);
            TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, accountTbl);
            System.out.printf("%s%n", sendResult);

        }
    }

}
