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
    }

    public static void main(String args[]) {
        ClientSdk clientSdk = new ClientSdkBuilder()
                .setHost("localhost")
                .setPort(8080)
                .build();

        clientSdk.init();
        for (int i = 0; i < 10; ++i) {
            clientSdk.send("sample", "message");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {

            }
        }
    }

    public void init() {
        astroConnector.init(port, host);
        astroConnector.connect();

        sender = new Sender(astroConnector);
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
