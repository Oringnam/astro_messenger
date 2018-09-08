package astro.grpc.clientsdk;

import astro.com.message.AstroMessage;
import astro.com.message.Return;
import astro.com.message.TransportGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import message.AstroCoder;
import message.MessageFormat;
import monitor.AstroMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client implements MessageFormat {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private AtomicBoolean opener = new AtomicBoolean(true);
    private AstroMonitor astromonitor = new AstroMonitor();

    private ManagedChannel channel;
    private TransportGrpc.TransportBlockingStub nonBlockingStub;
    private LinkedBlockingQueue<astro.com.message.AstroMessage> messageQueue = new
            LinkedBlockingQueue<astro.com.message.AstroMessage>();

    /*** Grpc 전송 ***/
    public Client(String host, int port) {
        boolean connectSwitch = connect(host, port);

        if (connectSwitch) {
            threadPool();
        } else {
            logger.error("Connetion Error");
        }
    }

    public static void main(String[] args) {
        String host = Basic.getHostIp();

        Client client = new Client(host, 8080);

        for (int index = 0; index < 10; ++index) {
            Long time = System.currentTimeMillis();
            try {
                TimeUnit.MILLISECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String topic = "test";
            String message = "testMessage";
            String uuid = AstroCoder.getUniqueId(time, message);

            astro.com.message.AstroMessage astroMessage = client.makeMessage(index, time, topic, message, uuid);
            try {
                client.sendMessage(astroMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.close();
    }

    private boolean connect(String host, int port) {
        if (host == null) {
            logger.error("Server not found");
            return false;
        }

        try {
            this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            this.nonBlockingStub = TransportGrpc.newBlockingStub(channel);
        } catch (Exception e) {
            logger.error("Connection fail");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void threadPool() {
        service.submit(() -> {
            while (opener.get()) {
                try {
                    Object object = messageQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (object != null) {
                        Return result = nonBlockingStub.sendMessage((astro.com.message.AstroMessage) object);
                        astromonitor.increaseTransferMessageCount();
                        while (result.getReturnCode() == 1) {
                            astromonitor.failedTransferMessageCount(((astro.com.message.AstroMessage) object)
                                    .getIndex());
                            result = nonBlockingStub.sendMessage((astro.com.message.AstroMessage) object);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendMessage(astro.com.message.AstroMessage message) {
        try {
            messageQueue.put(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AstroMessage makeMessage(int index, long time, String topic, String message, String uuid) {
        try {
            try {
                validator(topic);
            } catch (Exception e) {
                logger.error("Invalid topic");
                e.printStackTrace();
                return null;
            }

            try {
                validator(message);
            } catch (Exception e) {
                logger.error("Invalid astro.grpc.clientsdk.message");
                e.printStackTrace();
                return null;
            }

            astro.com.message.AstroMessage.Builder astroMessage = astro.com.message.AstroMessage.newBuilder();
            astroMessage.setIndex(index);
            astroMessage.setDatetime(time);
            astroMessage.setTopic(topic);
            astroMessage.setMessage(message);
            astroMessage.setUuid(uuid);

            astromonitor.increaseMessagecCount();
            return astroMessage.build();
        } catch (Exception e) {
            logger.error("Message creation fail");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean validator(String value) throws Exception {
        if (value == null) {
            throw new Exception();
        }

        return false;
    }

    public void close() {
        opener.set(false);
        service.shutdown();
    }
}
