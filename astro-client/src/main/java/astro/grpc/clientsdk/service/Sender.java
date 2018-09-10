package astro.grpc.clientsdk.service;

import astro.com.message.AstroMessage;
import astro.com.message.Return;
import astro.grpc.clientsdk.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Sender {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService service = Executors.newFixedThreadPool(10);
    private MessageQueue messageQueue = new MessageQueue();
    private AstroConnector astroConnector;

    private AtomicBoolean opener = new AtomicBoolean(true);

    public Sender(AstroConnector astroConnector) {
        this.astroConnector = astroConnector;
        threadPool();
    }

    public void threadPool() {
        service.submit(()-> {
           while(opener.get()) {
               try {
                   AstroMessage value = messageQueue.poll(100, TimeUnit.MILLISECONDS);
                   if (value != null) {
                       Return result = astroConnector.getBlockingStub().sendMessage(value);

                       while (result.getReturnCode() == 1) {
                           result = astroConnector.getBlockingStub().sendMessage(value);
                       }
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        });

    }

    public void send(AstroMessage message) {
        try {
            messageQueue.put(message);
        } catch(Exception e) {
            logger.error("Failed to transfer message");
        }
    }
}
