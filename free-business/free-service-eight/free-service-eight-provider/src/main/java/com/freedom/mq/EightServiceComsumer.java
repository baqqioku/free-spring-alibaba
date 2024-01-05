package com.freedom.mq;

import com.github.jaskey.rocketmq.core.DedupConcurrentListener;
import com.github.jaskey.rocketmq.core.DedupConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

@Slf4j
public class EightServiceComsumer extends DedupConcurrentListener {

    public EightServiceComsumer(DedupConfig dedupConfig) {
        super(dedupConfig);
    }

    protected String dedupMessageKey(final MessageExt messageExt) {
        return super.dedupMessageKey(messageExt);

    }
        @Override
    protected boolean doHandleMsg(MessageExt messageExt) {
        switch (messageExt.getTopic()){
            case "guoguo":{
                log.info("假装消费很久....{} {}", new String(messageExt.getBody()), messageExt);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {}
                break;
            }
        }
        return true;
    }
}
