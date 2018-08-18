package astro.grpc.server;

import astro.grpc.server.basic.ServerQueue;
import astro.grpc.server.controller.MariaManager;
import astro.grpc.server.controller.ServerManager;
import monitor.AstroMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    public static AstroMonitor astromonitor = new AstroMonitor();
    private ExecutorService service = Executors.newScheduledThreadPool(10);
    private AtomicBoolean opener = new AtomicBoolean(true);
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ServerManager serverManager = new ServerManager();
    private MariaManager mariaManager = new MariaManager();

    private ServerQueue queue;
    private MessageImplementation messageImplementation;

    private int queueSize = 500000;

    public Server() {
        init();
//        threadPool();
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

/*
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
*/

    private boolean init() {
        boolean connectionSwitch = serverManager.launching();
        if (!connectionSwitch) {
            logger.error("astro.grpc.server.Server Connection Error");
            return false;
        }

        connectionSwitch = mariaManager.connect();
        if (!connectionSwitch) {
            logger.error("Database Connection Error");
            return false;
        }

        queue = new ServerQueue.ServerQueueBuilder().setMaxSize(queueSize).build();
        messageImplementation = new MessageImplementation(queue);

        return true;
    }

    public void close() {
        opener.set(false);
        service.shutdown();
    }
}
