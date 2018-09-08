package astro.grpc.server.controller;

import astro.grpc.server.basic.AstroJobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class WorkerManager {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private ExecutorService service;

    private AstroJobs astroJobs;

    private int workers;

    private WorkerManager(WorkerManagerBuilder builder) {

        if (builder.astroJobs == null) {
            logger.warn("WorkerManager is failed to load AstroJobs");
        }
        this.astroJobs = builder.astroJobs;

        this.workers = builder.workers;

        service = Executors.newFixedThreadPool(this.workers);
    }

    public void workerPool() {
        service.submit(astroJobs);
    }

    public static class WorkerManagerBuilder {
        private int workers = 10;

        private AstroJobs astroJobs;

        public WorkerManagerBuilder setWorkers(int workers) {
            this.workers = workers;
            return this;
        }

        public WorkerManagerBuilder setAstroJobs(AstroJobs astroJobs) {
            this.astroJobs = astroJobs;
            return this;
        }

        public WorkerManager build() {
            return new WorkerManager(this);
        }
    }

}
