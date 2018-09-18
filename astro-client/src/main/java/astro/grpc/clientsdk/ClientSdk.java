package astro.grpc.clientsdk;

import astro.com.message.AstroMessage;
import astro.grpc.clientsdk.message.MessageBuilder;
import astro.grpc.clientsdk.service.AstroConnector;
import astro.grpc.clientsdk.service.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ClientSdk {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private MessageBuilder messageBuilder;
    private AstroConnector astroConnector;
    private Sender sender;

    private int port;
    private String host;

    private ClientSdk(ClientSdkBuilder builder) {
        port = builder.port;
        host = builder.host;
        messageBuilder = new MessageBuilder();
        astroConnector = new AstroConnector();

        astroConnector.init(port, host);
        boolean connectSwitch = astroConnector.connect();

        if(connectSwitch) {
            sender = new Sender(astroConnector);
        } else {
            logger.error("Connection error.");
        }


    }

    public static void main(String args[]) {
        ClientSdk clientSdk = new ClientSdkBuilder()
                .setHost("localhost")
                .setPort(8082)
                .build();

        clientSdk.send("weather", "한글");

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void send(String topic, String message) {
        AstroMessage sendingMessage = messageBuilder.makeMessage(topic, message);
        sender.send(sendingMessage);
    }

    public static class ClientSdkBuilder {
        private int port;
        private String host;

        public ClientSdkBuilder setPort(int port) {
            this.port = port;

            return this;
        }

        public ClientSdkBuilder setHost(String host) {
            this.host = host;

            return this;
        }

        public ClientSdk build() {
            return new ClientSdk(this);
        }
    }
}
