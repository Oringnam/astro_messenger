import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import message.AstroMessage;
import monitor.AstroMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private ExecutorService service = Executors.newScheduledThreadPool(10);
    private LinkedBlockingQueue<AstroMessage> queue = new LinkedBlockingQueue<>();
    private AtomicBoolean opener = new AtomicBoolean(true);
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static AstroMonitor astromonitor = new AstroMonitor();

    private MessageImplementation messageImplementation = new MessageImplementation();
    private ConnectionManager connectionManager = new ConnectionManager();
    private StoringManager storingManager = new StoringManager();

    public Server() {
        threadPool();
    }

    private boolean connect(int port) {
        boolean connectionSwitch = connectionManager.clientCoennect(messageImplementation, 8080);
        if(!connectionSwitch){
            logger.error("Server Connection Error");

            return false;
        }

        connectionSwitch = connectionManager.databaseConnect();
        if(!connectionSwitch){
            logger.error("Database Connection Error");

            return false;
        }

        return true;
    }

    private void threadPool()  {
        service.submit(()-> {
            while(opener.get()) {
                try {
                    Object value = messageImplementation.queue.poll(100, TimeUnit.MILLISECONDS);
                    if(value == null) {
                        continue;
                    } else {
                        astromonitor.increaseMessagecCount();
                    }
                    storingManager.store(connectionManager.dbConnector, value);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void close() {
        opener.set(false);
        service.shutdown();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.connect(8080);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //server.close();
    }

}
