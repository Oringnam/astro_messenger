package astro.grpc.clientsdk;

import astro.com.message.AstroMessage;

public interface ClientSdk {
    public void send(String topic, String message);
}
