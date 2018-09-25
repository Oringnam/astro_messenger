package astro.grpc.clientsdk.service;

import astro.com.message.TransportGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstroConnector {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ManagedChannel channel = null;
    private TransportGrpc.TransportBlockingStub blockingStub = null;

    private int port;
    private String host;

    public void init(int port, String host) {
        this.port = port;
        this.host = host;

        initDisplay();
    }

    private void initDisplay() {
        logger.info("Client Connector Initialization-----------------");
        logger.info("Host : {}", host);
        logger.info("port : {}", port);
        logger.info("------------------------------------------------");
    }

    public boolean connect() {
        if (host == null) {
            logger.error("Server not found");

            return false;
        }

        //연결 오류처리
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = TransportGrpc.newBlockingStub(channel);
        //연결 오류처리

        return true;
    }

    public TransportGrpc.TransportBlockingStub getBlockingStub() {
        return blockingStub;
    }


}
