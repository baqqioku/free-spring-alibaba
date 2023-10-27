package com.free.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.InetSocketAddress;
import java.util.List;

public class CanalDemoApplicationTests {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "canal:test:";

    @Test
    public void contextLoads() {
        // 创建链接，connector也是canal数据操作客户端，默认端口号：11111
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1", 11110), "example", "root", "root");
        int batchSize = 1000;
        int emptyCount = 0;
        try {
            // 链接对应的canal server
            connector.connect();
            // 客户端订阅，重复订阅时会更新对应的filter信息，这里订阅所有库的所有表
            connector.subscribe(".*\\..*");
            // 回滚到未进行 ack 的地方，下次fetch的时候，可以从最后一个没有 ack 的地方开始拿
            connector.rollback();

            while (true) {
                // 尝试拿batchSize条记录，有多少取多少，不会阻塞等待

                Message message = connector.getWithoutAck(batchSize);
                // 消息id
                long batchId = message.getId();
                // 实际获取记录数
                int size = message.getEntries().size();
                // 如果没有获取到消息
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    System.out.println("empty count : " + emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    // 如果消息不为空，重置遍历。从0开始重新遍历
                    emptyCount = 0;
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    printEntry(message.getEntries());
                }

                // 进行 batch id 的确认。
                connector.ack(batchId); // 提交确认
                // 回滚到未进行 ack 的地方，指定回滚具体的batchId；如果不指定batchId，回滚到未进行ack的地方
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }

        } finally {
            // 释放链接
            connector.disconnect();
        }
    }

    private void printEntry(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            // 如果是事务操作，直接忽略。 EntryType常见取值：事务头BEGIN/事务尾END/数据ROWDATA
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            // 如果不是需要数据同步的表，直接忽略。
            if (!StringUtils.equals(entry.getHeader().getSchemaName(), "test") || !StringUtils.equals(entry.getHeader().getTableName(), "user")) {
                continue;
            }

            CanalEntry.RowChange rowChange = null;
            try {
                // 获取byte数据，并反序列化
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            CanalEntry.EventType eventType = rowChange.getEventType();

            // 如果是删除、更新、新增操作解析出数据
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                // 操作前数据
                List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                // 操作后数据
                List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                if (eventType == CanalEntry.EventType.DELETE) {
                    // 删除操作，只有删除前的数据
                    if (beforeColumnsList.size() <= 0) {
                        continue;
                    }
                    for (CanalEntry.Column column : beforeColumnsList) {
                        // 取主键作为key删除对应的缓存
                        if (column.getIsKey()) {
                            this.redisTemplate.delete(KEY_PREFIX + column.getValue());
                        }
                    }
                } else {
                    // 新增/更新数据，取操作后的数据。组装成json数据
                    if (afterColumnsList.size() <= 0) {
                        continue;
                    }
                    JSONObject json = new JSONObject();
                    // 主键
                    String key = null;
                    for (CanalEntry.Column column : afterColumnsList) {
                        // 遍历字段放入json
                        json.put(underscoreToCamel(column.getName()), column.getValue());
                        // 如果是该字段是主键，取出该字段
                        if (column.getIsKey()) {
                            key = column.getValue();
                        }
                    }
                    this.redisTemplate.opsForValue().set(KEY_PREFIX + key, json.toJSONString());
                }
            }
        }
    }

    /**
     * 下划线 转 驼峰
     *
     * @param param
     * @return
     */
    private String underscoreToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = Character.toLowerCase(param.charAt(i));
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

