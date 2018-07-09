import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import message.AstroCoder;
import message.AstroMessage;
import monitor.AstroMonitor;
import network.RMI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AstroProperties;
import utils.Basic;

public class Client {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private LinkedBlockingQueue<AstroMessage> queue = new LinkedBlockingQueue<AstroMessage>();
    private AtomicBoolean opener = new AtomicBoolean(true);
    private RMI stub;
    private int messageCount;
    private int messageTransferCount;
    private AstroMonitor astromonitor  = new AstroMonitor();


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
                    Object object = queue.poll(100, TimeUnit.MILLISECONDS);
                    if (object != null) {
                        stub.messaging((AstroMessage) object);
                        astromonitor.increaseMessagecCount();
                    }
                } catch (RemoteException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void send(AstroMessage message) {

        try {
            queue.put(message);
            astromonitor.increaseTransferMessageCount();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
    public int getMessageCount() {
        return messageCount;
    }
    public void setMessageTransferCount(int messageTransferCount) { this.messageTransferCount = messageTransferCount; }
    public int getMessageTransferCount() {
        return messageTransferCount;
    }


    public void close() {
        opener.set(false);
        service.shutdown();
    }

    public static void main(String[] args) {
        String host = Basic.getHostIp();

        Client client = new Client();
        client.connect(host);

        String message = "testMessage";
        Long time = System.currentTimeMillis();

        AstroMessage astroMessage = new AstroMessage();

        astroMessage.setDatetime(time);
        astroMessage.setIndex(0);
        try {
            astroMessage.setTopic("test");
            astroMessage.validator(astroMessage.getTopic());
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            astroMessage.setMessage(message);
            astroMessage.validator(astroMessage.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
        }


        String uuid = AstroCoder.getUniqueId(time, message);
        astroMessage.setUuid(uuid);
        //client.send(astroMessage);

        for(int i=0; i<10; i++) {
            client.send(astroMessage);
        }

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
