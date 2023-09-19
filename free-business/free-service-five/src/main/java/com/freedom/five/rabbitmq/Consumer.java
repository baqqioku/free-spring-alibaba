package com.freedom.five.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private static final String QUEUE_NAME = "hello";


    public ConnectionFactory getConnectionFactory(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.126.130");
        connectionFactory.setPort(5673);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        return connectionFactory;
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        ConnectionFactory factory = consumer.getConnectionFactory();

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // 设置消费者的消息确认模式为手动确认
            channel.basicConsume(QUEUE_NAME, false, "my_consumer", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("接收到消息：" + message);

                    // 处理消息并模拟处理失败的情况
                    boolean success = processMessage(message);

                    if (success) {
                        // 手动确认消息
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        System.out.println("消息处理成功，已确认");
                    } else {
                        // 手动拒绝消息并重新入队
                        channel.basicReject(envelope.getDeliveryTag(), true);
                        System.out.println("消息处理失败，已拒绝并重新入队");
                    }
                }
            });

            // 持续监听队列中的消息
            /*while (true) {
                channel.waitForConfirms();
            }*/
            //channel.waitForConfirms();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static boolean processMessage(String message) {
        // 这里可以根据业务逻辑处理消息，返回处理结果
        // 模拟处理成功的情况
        return true;
    }
}
