package monitor;

import java.util.concurrent.atomic.AtomicInteger;

public class AstroMonitor {
    private AtomicInteger messageCount;
    private AtomicInteger transferMessageCount;

    public AstroMonitor() {
        messageCount = new AtomicInteger(0);
        transferMessageCount = new AtomicInteger(0);
    }

    public void increaseMessagecCount() {
        messageCount.set(messageCount.get()+1);
    }

    public void increaseTransferMessageCount() {
        transferMessageCount.set(transferMessageCount.get()+1);
    }


}
