package monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class AstroMonitor {
    private AtomicInteger messageCount;
    private AtomicInteger transferMessageCount;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public AstroMonitor() {
        messageCount = new AtomicInteger(0);
        transferMessageCount = new AtomicInteger(0);
    }

    public void increaseMessagecCount() {
        messageCount.set(messageCount.get()+1);
        logger.error("Message created : " + messageCount.get());
    }

    public void increaseTransferMessageCount() {
        transferMessageCount.set(transferMessageCount.get()+1);
        logger.error("Message transfered : " + transferMessageCount.get());
    }


}
