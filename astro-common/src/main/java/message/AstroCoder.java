package message;

import java.nio.ByteBuffer;
import java.util.UUID;

public class AstroCoder {
    public static String getUniqueId(Long datetime, String message) {

        byte[] messageBytes = message.getBytes();
        ByteBuffer buf = ByteBuffer.allocate(8);
        byte[] timeBytes = buf.putLong(datetime).array();

        byte[] input = new byte[messageBytes.length + timeBytes.length];

        System.arraycopy(timeBytes, 0, input, 0, timeBytes.length);
        System.arraycopy(messageBytes, 0, input, timeBytes.length, messageBytes.length);

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
