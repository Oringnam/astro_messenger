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
    private AstroConnectorBuilder astroConnectorBuilder;
    private AstroConnector astroConnector;
    private Sender sender;

    private ClientSdk() {
        messageBuilder = new MessageBuilder();
        astroConnectorBuilder = new AstroConnectorBuilder();
    }

    public static void main(String args[]) {
        ClientSdk clientSdk = new ClientSdk();
        clientSdk.astroConnector = clientSdk.astroConnectorBuilder.setPort(8080)
                                                                  .setHost("localhost")
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
        astroConnector.initDisplay();
        astroConnector.connect();

        sender = new Sender(astroConnector);
    }

    public void send(String topic, String message) {
        AstroMessage sendingMessage = messageBuilder.makeMessage(topic, message);
        sender.send(sendingMessage);
    }

    public class AstroConnectorBuilder {
        private int port;
        private String host;

        public int getPort() { return port; }

        public AstroConnectorBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public String getHost() { return host; }

        public AstroConnectorBuilder setHost(String host) {
            this.host = host;
            return this;
        }

        public AstroConnector build() { return new AstroConnector(this); }

        ;
    }
}
