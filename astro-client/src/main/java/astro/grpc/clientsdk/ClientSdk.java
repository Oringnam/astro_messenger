package astro.grpc.clientsdk;

import astro.grpc.clientsdk.service.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSdk {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    Sender sender;


    private ClientSdk() {

    }

    public void init() {

    }

    public void send() {
        sender.send();
    }
}
