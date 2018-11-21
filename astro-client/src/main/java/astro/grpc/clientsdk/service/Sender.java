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

public class Sender implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService service = Executors.newFixedThreadPool(10);
    private MessageQueue messageQueue = new MessageQueue();
    private AstroConnector astroConnector;
    private int messageErrorCounter;

    private AtomicBoolean opener = new AtomicBoolean(true);

    public Sender(AstroConnector astroConnector) {
        this.astroConnector = astroConnector;
        messageErrorCounter = 0;
        run();
    }

    @Override
    public void run() {
        service.submit(() -> {
            AstroMessage value = null;
            while (opener.get()) {
                try {
                    value = messageQueue.poll(100, TimeUnit.MILLISECONDS);

                    if(messageErrorCounter > 10) {
                        logger.warn("Connection Error. Please check the connection");
                    }

                    if (value != null) {
                        Return result = astroConnector.getBlockingStub()
                                                      .withDeadlineAfter(5000, TimeUnit.MILLISECONDS)
                                                      .sendMessage(value);
                        logger.info("Message transfered : {}", value.getIndex());

                        if (result.getReturnCode() != Return.successCode.Success_VALUE) {
                            errorCodeChecker(result.getReturnCode());

                            messageQueue.put(value);
                        } else {
                            messageErrorCounter = 0;
                        }
                    }

                } catch (RuntimeException e) {
                    logger.error("Message transfer error : {}", value.getIndex());
                    messageErrorCounter++;
                    messageQueue.put(value);
                } catch (Exception e) {
                    logger.error("Cannot transfer message");
                    messageErrorCounter++;
                    messageQueue.put(value);
                }
            }
        });
    }

    public void send(AstroMessage message) {
        messageQueue.put(message);
    }

    private void errorCodeChecker(int errorCode) {
        switch (errorCode) {
            case Return.messageErrorCode.AstroMessage_Null_VALUE : logger.warn("Error code [{}] : {}", errorCode, Return.messageErrorCode.AstroMessage_Null);
                break;
            case Return.messageErrorCode.Uuid_Null_VALUE : logger.warn("Error code [{}] : {}", errorCode, Return.messageErrorCode.Uuid_Null);
                break;
            case Return.messageErrorCode.Topic_Null_VALUE : logger.warn("Error code [{}] : {}", errorCode, Return.messageErrorCode.Topic_Null);
                break;
            case Return.messageErrorCode.Message_Null_VALUE : logger.warn("Error code [{}] : {}", errorCode, Return.messageErrorCode.Message_Null);
                break;
            case Return.messageErrorCode.Date_Zero_VALUE : logger.warn("Error code [{}] : {}", errorCode, Return.messageErrorCode.Date_Zero);
                break;
            case Return.messageErrorCode.Index_Invalid_VALUE : logger.warn("Error code [{}] : {}", errorCode, Return.messageErrorCode.Index_Invalid);
                break;

            case Return.storingErrorCode.Queue_Full_VALUE : logger.warn("Error code [{}] : {}", errorCode, Return.storingErrorCode.Queue_Full);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

             case Return.ErrorCode.Unrecognized_Error_VALUE : logger.warn("Error code [{}] : {}", errorCode, Return.ErrorCode.Unrecognized_Error);
                break;
        }
    }

}
