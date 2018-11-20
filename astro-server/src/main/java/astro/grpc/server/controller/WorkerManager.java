package astro.grpc.server.controller;

import astro.grpc.server.basic.AstroJobs;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Builder
@Log4j2
public class WorkerManager {
    private AstroJobs astroJobs;

    @Builder.Default
    private int workers = 10;

    @Builder.Default
    private ExecutorService service = Executors.newFixedThreadPool(10);


    public void workerPool() {
        service = Executors.newFixedThreadPool(workers);
        service.submit(astroJobs);
    }

    public void jobsNullChecker() {
        if (astroJobs == null) {
            log.warn("WorkerManager is failed to load AstroJobs");
        }
    }

}
