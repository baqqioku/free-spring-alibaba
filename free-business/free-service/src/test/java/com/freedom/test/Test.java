package com.freedom.test;

import com.freedom.FreeServiceApplication;
import com.freedom.model.mapper.UserMapper;
import com.freedom.transaction.MultiplyThreadTransactionManager;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@SpringBootTest(classes = FreeServiceApplication.class)
public class Test {
    @Resource
    private UserMapper userMapper;

    @Resource
    private MultiplyThreadTransactionManager multiplyThreadTransactionManager;

    @SneakyThrows
    @org.junit.jupiter.api.Test
    public void test(){
        List<Runnable> tasks=new ArrayList<>();

        tasks.add(()->{
            userMapper.deleteByPrimaryKey(1L);
            throw new RuntimeException("我就要抛出异常!");
        });



        multiplyThreadTransactionManager.runAsyncButWaitUntilAllDown(tasks, Executors.newCachedThreadPool());
    }

}
