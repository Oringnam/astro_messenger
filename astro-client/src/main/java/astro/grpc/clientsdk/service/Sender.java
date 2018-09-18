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
        service.submit(() -> {
            AstroMessage value = null;
            while (opener.get()) {
                try {
                    value = messageQueue.poll(100, TimeUnit.MILLISECONDS);

                    if (value != null) {
                        Return result = astroConnector.getBlockingStub().sendMessage(value);
                        logger.debug("Message transfered : {}", value);

                        // 너무 위험함, 계속 전송 안되면 행걸려

                        if (result.getReturnCode() != Return.successCode.Success_VALUE) {
                            errorCodeChecker(result.getReturnCode());

                            messageQueue.put(value);
                        }
                    }

                } catch (RuntimeException e) {
                    logger.error("Message transter error : {}", value.getIndex());
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
