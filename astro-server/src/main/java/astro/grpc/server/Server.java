package astro.grpc.server;

import astro.grpc.server.basic.AstroJobs;
import astro.grpc.server.basic.ServerQueue;
import astro.grpc.server.controller.MariaManager;
import astro.grpc.server.controller.ServerManager;
import astro.grpc.server.controller.WorkerManager;
import monitor.AstroMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Server {
    public static AstroMonitor astromonitor = new AstroMonitor();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ServerManager serverManager = new ServerManager();
    private MariaManager mariaManager = new MariaManager();

    private ServerQueue queue;
    private GrpcService grpcService;

    private int queueSize = 500000;

    private WorkerManager workerManager;
    private AstroJobs astroJobs;

    private int workers = 10;

    public Server() {
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
        queue = new ServerQueue.ServerQueueBuilder().setMaxSize(queueSize).build();
        grpcService = new GrpcService(queue);

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
        astroJobs = new AstroJobs.AstroJobsBuilder()
                .setMariaManager(mariaManager)
                .setServerQueue(queue)
                .build();

        workerManager = new WorkerManager.WorkerManagerBuilder()
                .setWorkers(workers)
                .setAstroJobs(astroJobs)
                .build();

        workerManager.workerPool();
    }

    public void close() {

    }
}
