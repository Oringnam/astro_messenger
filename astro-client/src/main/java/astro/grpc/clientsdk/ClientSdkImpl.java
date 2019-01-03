package astro.grpc.clientsdk;

import astro.com.message.AstroMessage;
import astro.grpc.clientsdk.message.MessageBuilder;
import astro.grpc.clientsdk.service.AstroConnector;
import astro.grpc.clientsdk.service.Sender;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Builder
@Slf4j
public class ClientSdkImpl implements ClientSdk {
    @Builder.Default
    private MessageBuilder messageBuilder;
    private AstroConnector astroConnector;
    private Sender sender;

    private int port;
    private String host;

    public void connect() {
        messageBuilder = new MessageBuilder();
        astroConnector = new AstroConnector();
        astroConnector.init(port, host);
        boolean connectSwitch = astroConnector.connect();

        if(connectSwitch) {
            sender = new Sender(astroConnector);
        } else {
            log.error("Connection error.");
        }
    }

//    public static void main(String args[]) {
//        ClientSdk clientSdk = ClientSdkImpl.builder()
//                                           .host("localhost")
//                                           .port(8082)
//                                           .build();
//        clientSdk.send("test", "test");
//
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return;
//    }

    public void send(String topic, String message) {
        AstroMessage sendingMessage = messageBuilder.makeMessage(topic, message);
        sender.send(sendingMessage);
    }

}
