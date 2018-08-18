package astro.grpc.server.controller;

import astro.grpc.server.GrpcService;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private io.grpc.Server server = null;

    private int port = 8080;

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
