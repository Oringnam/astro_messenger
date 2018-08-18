package astro.grpc.server.basic;

import astro.com.message.AstroMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class ServerQueue {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private int maxSize;
    private LinkedBlockingQueue<AstroMessage> queue;

    private ServerQueue(ServerQueueBuilder builder) {
        this.maxSize = builder.maxSize;
        this.queue = builder.queue;
    }

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
            return false;
        }

        try {
            queue.put(message);
        } catch (InterruptedException e) {
            logger.info("Message enqueue is failed : {}", e.getMessage());
        }

        return true;
    }


    public static class ServerQueueBuilder {
        // default 50만
        private int maxSize = 500000;

        private LinkedBlockingQueue<AstroMessage> queue = new LinkedBlockingQueue<>();

        public ServerQueueBuilder setMaxSize(int size) {
            this.maxSize = size;
            return this;
        }

        public ServerQueue build() {
            return new ServerQueue(this);
        }
    }
}
