package astro.grpc.clientsdk;

import astro.com.message.AstroMessage;
import astro.grpc.clientsdk.config.AppConfig;
import astro.grpc.clientsdk.message.MessageBuilder;
import astro.grpc.clientsdk.service.AstroConnector;
import astro.grpc.clientsdk.service.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSdk {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static AppConfig config;
    private MessageBuilder messageBuilder;
    private AstroConnector astroConnector;
    public Sender sender;

    private ClientSdk() {
        config = new AppConfig("client.properties");
        messageBuilder = new MessageBuilder();
        astroConnector = new AstroConnector();
    }

    public void init() {
        astroConnector.connect();
        astroConnector.init();
    }

    public void send(String topic, String message) {
        AstroMessage sendingMessage = messageBuilder.makeMessage(topic, message);
        sender.send(sendingMessage);
    }
}
