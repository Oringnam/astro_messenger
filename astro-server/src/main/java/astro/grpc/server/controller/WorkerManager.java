package astro.grpc.server.controller;

import astro.com.message.AstroMessage;
import astro.grpc.server.basic.ServerQueue;
import monitor.AstroMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerManager {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private ExecutorService service;

    private ServerQueue queue;

    private AstroMonitor astroMonitor;

    private MariaManager mariaManager;

    private int workers;

    private WorkerManager(WorkerManagerBuilder builder) {

        if (builder.queue == null) {
            logger.warn("WorkerManager is failed to load ServerQueue");
        }
        this.queue = builder.queue;

        this.workers = builder.workers;

        if(builder.astroMonitor == null) {
            logger.warn("WorkerManager is failed to load AstroMonitor");
        }
        this.astroMonitor = builder.astroMonitor;

        if(builder.mariaManager == null) {
            logger.warn("WorkerManager is failed to load MariaManager");
        }
        this.mariaManager = builder.mariaManager;

        service = Executors.newFixedThreadPool(this.workers);
    }

    public void workerPool() {
        AstroJob job = new AstroJob();
        service.submit(job);
    }

    public static class WorkerManagerBuilder {
        private int workers = 10;

        private ServerQueue queue;

        private AstroMonitor astroMonitor;

        private MariaManager mariaManager;

        public WorkerManagerBuilder setWorkers(int workers) {
            this.workers = workers;
            return this;
        }

        public WorkerManagerBuilder setServerQueue(ServerQueue queue) {
            this.queue = queue;
            return this;
        }

        public WorkerManagerBuilder setAstroMonitor(AstroMonitor astroMonitor) {
            this.astroMonitor = astroMonitor;
            return this;
        }

        public WorkerManagerBuilder setMariaManager(MariaManager mariaManager) {
            this.mariaManager = mariaManager;
            return this;
        }

        public WorkerManager build() {
            return new WorkerManager(this);
        }
    }

    private class AstroJob implements Runnable {
        private AtomicBoolean opener = new AtomicBoolean(true);

        @Override
        public void run() {
            while (opener.get()) {
                try {
                    AstroMessage astroMessage = queue.poll(100);
                    if (astroMessage == null) {
                        continue;
                    } else {
                        astroMonitor.increaseMessagecCount();
                    }

                    mariaManager.store(mariaManager.dbConnector, astroMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
