package astro.grpc.server.controller;

import astro.grpc.server.basic.AstroJobs;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Builder
public class WorkerManager {
    Logger logger;
    private ExecutorService service;
    private AstroJobs astroJobs;
    private int workers = 10;


    public void workerPool() {
        service.submit(astroJobs);
    }

    public void jobsNullChecker() {
        if (astroJobs == null) {
            logger.warn("WorkerManager is failed to load AstroJobs");
        }
    }

}
