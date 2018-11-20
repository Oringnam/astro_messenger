package astro.grpc.server.basic;

import astro.com.message.AstroMessage;
import astro.grpc.server.controller.MariaManager;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicBoolean;

@Builder
@Log4j2
public class AstroJobs implements Runnable {
    @Builder.Default
    private AtomicBoolean opener = new AtomicBoolean(true);
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
            log.warn("AstroJbosClass failed to load ServerQueue");
        }
    }

    public void mariaNullChecker() {
        if(mariaManager == null) {
            log.warn("AstroJbosClass failed to load MariaManager");
        }
    }

}

