import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import message.AstroMessage;
import network.RMI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AstroProperties;

public class Server implements RMI {
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private LinkedBlockingQueue<AstroMessage> queue = new LinkedBlockingQueue<>();
    private AtomicBoolean opener = new AtomicBoolean(true);

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Server() {
        threadPool();
    }

    @Override
    public AstroMessage messaging(AstroMessage message) {
        return message;
    }

    @Override
    public void messaging2(AstroMessage message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            logger.error("Server.messaging : {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void threadPool() {
        service.submit(() -> {
            while (opener.get()) {
                try {
                    Object value = queue.poll(100, TimeUnit.MILLISECONDS);
                    if (value == null) {
                        continue;
                    }
                    boolean stored = store(value);
                    if (!stored) {
                        throw new Exception("storing is failed");
                    }
                } catch (Exception e) {
                    logger.info("Server.threadPool.task : {}", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    // 저장 방식 정해지면, 구현해야함. db를 쓸것인지, 파일로 저장할 것인지
    private boolean store(Object value) {
        logger.info("message : {} ", value.toString());
        return true;
    }

    public void close() {
        opener.set(false);
        service.shutdown();
    }

    /**
     * getRegistry() 수행 시, gc가 registry 정리해버림.
     * createRegistry(port)로 수행할 것
     */
    public static void main(String[] args) {
        Server server = new Server();
        String serverName = AstroProperties.getProperty("server.name");

        try {
            RMI stub = (RMI) UnicastRemoteObject.exportObject(server, 1099);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(serverName, stub);
            System.out.println("Server test ready");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //server.close();
    }
}
