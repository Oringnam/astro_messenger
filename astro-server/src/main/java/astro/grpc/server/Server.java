package astro.grpc.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import astro.grpc.server.controller.MariaManager;
import astro.grpc.server.controller.ServerManager;
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
    private ServerManager serverManager = new ServerManager();
    private MariaManager mariaManager = new MariaManager();


    public Server() {
        init();
        threadPool();
    }

    private boolean init() {
        boolean connectionSwitch = serverManager.launching();
        if(!connectionSwitch){
            logger.error("astro.grpc.server.Server Connection Error");
            return false;
        }

        connectionSwitch = mariaManager.connect();
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
                    mariaManager.store(mariaManager.dbConnector, value);

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

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //server.close();
    }
}
