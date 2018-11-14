package astro.grpc.clientsdk.message;

import astro.com.message.AstroMessage;
import message.AstroCoder;

public class MessageBuilder {
    private int index;

    public MessageBuilder() {
        index = 0;
    }

    public AstroMessage makeMessage(String topic, String message) {
        Long time = System.currentTimeMillis();
        String uuid = AstroCoder.getUniqueId(time, message);

        AstroMessage.Builder messageBuilder = AstroMessage.newBuilder()
                                                          .setIndex(index)
                                                          .setDatetime(time)
                                                          .setTopic(topic)
                                                          .setMessage(message)
                                                          .setUuid(uuid);
        index++;

        return messageBuilder.build();
    }
}
