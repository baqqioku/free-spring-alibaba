package com.freedom.five.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Producer {

    public ConnectionFactory getConnectionFactory(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //connectionFactory.setHost("172.17.0.2");
        connectionFactory.setHost("192.168.126.130");
        connectionFactory.setPort(5673);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guoguo");
        connectionFactory.setPassword("guoguo");
        return connectionFactory;
    }

    public void send()  {
        ConnectionFactory connectionFactory = getConnectionFactory();
        Channel channel = null;
        try {
            channel = connectionFactory.newConnection().createChannel();
            channel.queueDeclare("hello",true,false,false,null);
            channel.txSelect();
            channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN,"hello".getBytes());
            System.out.println("消息发送成功");

            // 提交事务
            channel.txCommit();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            // 回滚事务
            try {
                channel.txRollback();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("消息发送失败，事务回滚");
        }

    }

    public static void main(String[] args){
        Producer producer = new Producer();
        producer.send();
    }
}
