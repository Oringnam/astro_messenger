package astro.grpc.clientsdk.service;

import astro.com.message.TransportGrpc;
import astro.grpc.clientsdk.ClientSdk;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstroConnector {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ManagedChannel channel = null;
    private TransportGrpc.TransportBlockingStub blockingStub = null;

    private int port = 8080;
    private String host = "localhost";

    public void init() {
        host = ClientSdk.config.get("server.host");
        String portKey = ClientSdk.config.get("server.port");
        port = Integer.parseInt(portKey);
        connect();
        initDisplay();
    }

    private void initDisplay() {
        logger.info("Client Connector Initialization-----------------");
        logger.info("Host : {}", host);
        logger.info("port {}", port);
        logger.info("------------------------------------------------");
    }

    public void connect() {
        if (host == null) {
            logger.error("Server not found");
        }

        try {
            this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            this.blockingStub = TransportGrpc.newBlockingStub(channel);
        } catch (Exception e) {
            logger.error("Connection fail. Cannot find server port");
        }
    }

    public TransportGrpc.TransportBlockingStub getBlockingStub() {
        return blockingStub;
    }
}
