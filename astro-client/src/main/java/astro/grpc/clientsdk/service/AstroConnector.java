package astro.grpc.clientsdk.service;

import astro.com.message.ACK;
import astro.com.message.Return;
import astro.com.message.TransportGrpc;
import astro.grpc.clientsdk.message.MessageBuilder;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstroConnector {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ManagedChannel channel = null;
    private TransportGrpc.TransportBlockingStub blockingStub = null;
    private MessageBuilder messageBuilder = new MessageBuilder();
    private int requestCode = 100;

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
        }

        //연결 오류처리
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = TransportGrpc.newBlockingStub(channel);

        try {
            ACK request = ACK.newBuilder().setACKCode(requestCode).build();
            Return result = blockingStub.sendACK(request);

            if(result.getReturnCode() == requestCode + 1) {
                logger.info("Connected to server");

                return true;
            }
        } catch(RuntimeException e) {
            logger.warn("Cannot connect to server");

            return false;
        }

        return false;
    }

    public TransportGrpc.TransportBlockingStub getBlockingStub() {
        return blockingStub;
    }


}
