import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import astro.com.samples.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import message.AstroCoder;
import message.AstroMessage;
import message.MessageFormat;
import monitor.AstroMonitor;
import network.RMI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AstroProperties;
import utils.Basic;

public class Client implements RMI, MessageFormat {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private LinkedBlockingQueue<AstroMessage> messageQueue = new LinkedBlockingQueue<AstroMessage>();
    private AtomicBoolean opener = new AtomicBoolean(true);
    private RMI stub;
    private AstroMonitor astromonitor  = new AstroMonitor();

    private ManagedChannel channel;
    private SampleGrpc.SampleBlockingStub blockingStub;
    private LinkedBlockingQueue<astro.com.samples.AstroMessage> sendingQueue = new LinkedBlockingQueue<astro.com.samples.AstroMessage>();


    public Client() {
        threadPool();
    }

    public boolean connect(String host) {
        if (host == null) {
            logger.error("Server not founded");
            return false;
        }

        String serverName = AstroProperties.getProperty("server.name");

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            stub = (RMI) registry.lookup(serverName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void threadPool() {
        service.submit(() -> {
            while (opener.get()) {
                try {
                    Object object = messageQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (object != null) {
                        stub.messaging((AstroMessage) object);
                        astromonitor.increaseTransferMessageCount();
                    }
                } catch (RemoteException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void send(AstroMessage message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messaging(AstroMessage astromessage) {
        try {
            logger.error("Message transfer failed : " + astromessage.getIndex());
            stub.messaging(astromessage);
        } catch(RemoteException e) {
            e.printStackTrace();
        }
    }


    /*** Grpc 전송 ***/

    public Client(String host, int port) {
        connect(host, port);
    }

    public void openClient() {
        service.submit(() -> {
            while (opener.get()) {
                try {
                    Object object = sendingQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (object != null) {
                        Result result = blockingStub.sendMessage((astro.com.samples.AstroMessage)object);
                        astromonitor.increaseTransferMessageCount();
                        while(result.getResultCode() == 1) {
                            astromonitor.failedTransferMessageCount(((astro.com.samples.AstroMessage) object).getIndex());
                            result = blockingStub.sendMessage((astro.com.samples.AstroMessage)object);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean connect(String host, int port) {
        if(host == null) {
            logger.error("Server not found");
            return false;
        }

        try {
            this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            this.blockingStub = SampleGrpc.newBlockingStub(channel);
        } catch(Exception e) {
            logger.error("Connection fail");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void send(astro.com.samples.AstroMessage message) {
        try {
            sendingQueue.put(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public astro.com.samples.AstroMessage makeMessage(int index, long time, String topic, String message, String uuid) {
        try {
            try {
                validator(topic);
            } catch(Exception e) {
                logger.error("Invalid topic");
                e.printStackTrace();
                return null;
            }

            try {
                validator(message);
            } catch (Exception e) {
                logger.error("Invalid message");
                e.printStackTrace();
                return null;
            }

            astro.com.samples.AstroMessage.Builder astroMessage = astro.com.samples.AstroMessage.newBuilder();
            astroMessage.setIndex(index);
            astroMessage.setDatetime(time);
            astroMessage.setTopic(topic);
            astroMessage.setMessage(message);
            astroMessage.setUuid(uuid);

            astromonitor.increaseMessagecCount();
            return astroMessage.build();
        } catch(Exception e) {
            logger.error("Message creation fail");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean validator(String value) throws Exception {
        if(value == null) {
            throw new Exception();
        }

        return false;
    }

    public void close() {
        opener.set(false);
        service.shutdown();
    }

    public static void main(String[] args) {
        String host = Basic.getHostIp();

        Client client = new Client(host, 8080);
        client.openClient();

        for(int index = 0; index < 10; ++index) {
            Long time = System.currentTimeMillis();
            String topic = "test";
            String message = "testMessage";
            String uuid = AstroCoder.getUniqueId(time, message);

            astro.com.samples.AstroMessage astroMessage = client.makeMessage(index, time, topic, message,uuid);
            try {
                client.send(astroMessage);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
