package astro.grpc.server.basic;

import astro.com.message.AstroMessage;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Builder
public class ServerQueue {
    private Logger logger;

    private int maxSize;
    private LinkedBlockingQueue<AstroMessage> queue;


    public boolean isFull() {
        if(maxSize <= queue.size()) {
            return true;
        }

        return false;
    }

    // 어떻게 처리할지 고민좀 해볼 것
    // 다수의 요청 --> 동기화 문제
    public synchronized boolean put(AstroMessage message) {
        if(isFull()) {
            logger.warn("Message queue is full");
            return false;
        }

        try {
            queue.put(message);
        } catch (InterruptedException e) {
            logger.info("Message enqueue is failed");

            return false;
        }

        return true;
    }

    public synchronized AstroMessage poll(long timeout) {
        Object value = null;

        try {
            value = queue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("Message dequeue is failed");

            return null;
        }

        return (AstroMessage) value;
    }

}
