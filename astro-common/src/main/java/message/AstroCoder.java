package message;

import java.util.UUID;

public class AstroCoder {
    public String getUniqueId(byte[] datetime, byte[] message) {
        byte[] input = new byte[message.length + datetime.length];

        System.arraycopy(datetime, 0, input, 0, datetime.length);
        System.arraycopy(message, 0, input, datetime.length, message.length);

        UUID uuid = UUID.nameUUIDFromBytes(input);
        return uuid.toString().replaceAll("-", "");
    }

    public static byte[] encode(String message) {
        return message.getBytes();
    }

    public static String decode(byte[] encodedMessage) {
        return new String(encodedMessage);
    }
}
