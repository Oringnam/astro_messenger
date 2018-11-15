package astro.grpc.server;

import astro.com.message.AstroMessage;
import astro.grpc.server.basic.AstroJobs;
import astro.grpc.server.basic.ServerQueue;
import astro.grpc.server.config.AppConfig;
import astro.grpc.server.controller.MariaManager;
import astro.grpc.server.controller.ServerManager;
import astro.grpc.server.controller.WorkerManager;
import monitor.AstroMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    public static AstroMonitor astromonitor = new AstroMonitor();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ServerManager serverManager = new ServerManager();
    private MariaManager mariaManager = new MariaManager();

    public static AppConfig config;


    private ServerQueue queue;
    private GrpcService grpcService;

    private int queueSize = 500000;

    private WorkerManager workerManager;
    private AstroJobs astroJobs;

    private int workers = 10;
    

    public Server() {
        config = new AppConfig("server.properties");

        init();
        runnalbe();
    }

    public static void main(String[] args) {
        Server server = new Server();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        server.close();
    }


    private boolean init() {
        queue = ServerQueue.builder()
                           .maxSize(queueSize)
                           .queue(new LinkedBlockingQueue<AstroMessage>())
                           .build();
        grpcService = new GrpcService(queue);

        serverManager.init();
        mariaManager.init();


        boolean connectionSwitch = serverManager.launching(grpcService);
        if (!connectionSwitch) {
            logger.error("astro.grpc.server.Server Connection Error");
            return false;
        }

        connectionSwitch = mariaManager.connect();
        if (!connectionSwitch) {
            logger.error("Database Connection Error");
            return false;
        }

        logger.info("AstroServer init finished");
        return true;
    }

    private void runnalbe() {
        astroJobs = AstroJobs.builder()
                             .mariaManager(mariaManager)
                             .queue(queue)
                             .opener(new AtomicBoolean(true))
                             .logger(LoggerFactory.getLogger(this.getClass()))
                             .build();

        astroJobs.queueNullChecker();
        astroJobs.mariaNullChecker();

        workerManager = WorkerManager.builder()
                                     .workers(workers)
                                     .astroJobs(astroJobs)
                                     .service(Executors.newFixedThreadPool(workers))
                                     .logger(LoggerFactory.getLogger(this.getClass()))
                                     .build();

        workerManager.jobsNullChecker();

        workerManager.workerPool();
    }

    public void close() {
        serverManager.getServer().shutdown();
    }
}
