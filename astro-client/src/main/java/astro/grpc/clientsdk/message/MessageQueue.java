package astro.grpc.clientsdk.message;

import astro.com.message.AstroMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageQueue {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LinkedBlockingQueue<AstroMessage> messageQueue;
    private int maxSize = 50000;

    public MessageQueue() {
        messageQueue = new LinkedBlockingQueue<astro.com.message.AstroMessage>();
    }

    public void setSize(int size) {
        maxSize = size;
    }

    private boolean isFull() {
        if(maxSize >= messageQueue.size()) {
            return false;
        }
        return true;
    }

    public synchronized boolean put(AstroMessage message) {
        if(isFull()) {
            logger.error("MessageQueue is full");
            return false;
        }

        try {
            messageQueue.put(message);
        } catch(Exception e) {
            logger.error("Failed to put data {} int queue", message);
        }

        return true;
    }

    public synchronized AstroMessage poll(long timeout, TimeUnit unit) {
        AstroMessage returnMessage = null;
        try {
            returnMessage = messageQueue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            logger.error("Failed to poll data in queue");
        }

        return returnMessage;
    }

}
