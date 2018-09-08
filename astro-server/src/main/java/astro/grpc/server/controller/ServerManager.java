package astro.grpc.server.controller;

import astro.grpc.server.GrpcService;
import astro.grpc.server.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private io.grpc.Server server = null;

    private int port = 8080;

    public void init() {
        try {
            String portValue = Server.config.get("server.port");
            port = Integer.parseInt(portValue);
        } catch (NullPointerException e) {
            logger.warn("config getter is failed : {}", e.getMessage());
        }

        initDisplay();
    }

    private void initDisplay() {
        logger.info("servermanager init --------");
        logger.info("port : {} ", port);

        logger.info("------------------\n");
    }

    public boolean launching(GrpcService grpcService) {
        try {
            server = ServerBuilder.forPort(port).addService(grpcService).build().start();
        } catch (IOException e) {
            logger.info("astro.grpc.server.Server open fail", e);
            return false;
        }

        logger.info("astro.grpc.server.Server is ready, {}", port);
        return true;
    }
}
