package utils;

public class AstroCoder {
    public static byte[] encode(String message) {
        return message.getBytes();
    }

    public static String decode(byte[] encodedMessage) {
        return new String(encodedMessage);
    }
}
