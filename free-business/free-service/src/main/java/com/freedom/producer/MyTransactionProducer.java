package com.freedom.producer;

import com.alibaba.fastjson.JSON;
import com.freedom.consumer.TransactionListenerImpl;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class MyTransactionProducer {

    @Value("${rocketmq.nameServer}")
    private String nameServer;

    private String producerGroup = "order_trans_group";
    private TransactionMQProducer producer;

    //用于执行本地事务和事务状态回查的监听器
    @Autowired
    TransactionListenerImpl orderTransactionListener;
    //执行任务的线程池
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));

    @PostConstruct
    public void init(){
        producer = new TransactionMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(Integer.MAX_VALUE);
        producer.setExecutorService(executor);
        producer.setTransactionListener(orderTransactionListener);
        this.start();
    }
    private void start(){
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
    //事务消息发送
    public TransactionSendResult send(String topic,String data,String businessNo) throws MQClientException, UnsupportedEncodingException {
        Message message = new Message(topic,data.getBytes());
        Message msg = new Message("guoguo-transaction", "tag", businessNo,
                data.getBytes(RemotingHelper.DEFAULT_CHARSET));
        TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, null);
        System.out.println("prepare事务消息发送结果:"+sendResult.getSendStatus());
        return sendResult;
        //return this.producer.sendMessageInTransaction(message, null);
    }
}
