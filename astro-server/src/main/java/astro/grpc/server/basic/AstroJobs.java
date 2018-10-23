package astro.grpc.server.basic;

import astro.com.message.AstroMessage;
import astro.grpc.server.controller.MariaManager;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@Builder
public class AstroJobs implements Runnable {
    private AtomicBoolean opener;
    private Logger logger;
    private ServerQueue queue;
    private MariaManager mariaManager;


    @Override
    public void run() {
        while (opener.get()) {

            AstroMessage astroMessage = queue.poll(100);
            if (astroMessage == null) {
                continue;
            }

            boolean storeSwitch = mariaManager.store(astroMessage);
            if(!storeSwitch) {      //저장 실패
                queue.put(astroMessage);
            }

        }
    }

    public void queueNullChecker() {
        if(queue == null) {
            logger.warn("AstroJbosClass failed to load ServerQueue");
        }
    }

    public void mariaNullChecker() {
        if(mariaManager == null) {
            logger.warn("AstroJbosClass failed to load MariaManager");
        }
    }

}

