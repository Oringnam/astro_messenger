package astro.grpc.server.basic;

import astro.com.message.AstroMessage;
import astro.grpc.server.controller.MariaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class AstroJobs implements Runnable {
    private AtomicBoolean opener = new AtomicBoolean(true);
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ServerQueue queue;
    private MariaManager mariaManager;

    public AstroJobs(AstroJobsBuilder builder) {
        if(builder.serverQueue == null) {
            logger.warn("AstroJbosClass failed to load ServerQueue");
        }
        this.queue = builder.serverQueue;

        if(builder.mariaManager == null) {
            logger.warn("AstroJbosClass failed to load MariaManager");
        }
        this.mariaManager = builder.mariaManager;
    }

    @Override
    public void run() {
        while (opener.get()) {
            try {
                AstroMessage astroMessage = queue.poll(100);
                if (astroMessage == null) {
                    continue;
                }

                mariaManager.store(astroMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class AstroJobsBuilder {
        private ServerQueue serverQueue;
        private MariaManager mariaManager;

        public AstroJobsBuilder setServerQueue(ServerQueue serverQueue) {
            this.serverQueue = serverQueue;
            return this;
        }

        public AstroJobsBuilder setMariaManager(MariaManager mariaManager) {
            this.mariaManager = mariaManager;
            return this;
        }

        public AstroJobs build() {
            return new AstroJobs(this);
        }
    }
}

