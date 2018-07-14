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

public class Client implements RMI {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private LinkedBlockingQueue<AstroMessage> messageQueue = new LinkedBlockingQueue<AstroMessage>();
    private AtomicBoolean opener = new AtomicBoolean(true);
    private RMI stub;
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

    public void close() {
        opener.set(false);
        service.shutdown();
    }

    public static void main(String[] args) {
        String host = Basic.getHostIp();

        Client client = new Client();
        client.connect(host);

        /*Message 생성, 송신*/
        AstroMessage astroMessage = new AstroMessage();

        for(int index = 0; index < 10000; ++index) {
            Long time = System.currentTimeMillis();
            String topic = "test";
            String message = "testMessage";
            String uuid = AstroCoder.getUniqueId(time, message);

            if(astroMessage.makeMessage(index, time, topic, message, uuid)) {
                client.astromonitor.increaseMessagecCount();
                client.send(astroMessage);
            } else {
                client.logger.error("Message error");
            }
        }

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
