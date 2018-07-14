package message;

public interface MessageFormat {
    boolean validator(String value) throws Exception;
    boolean makeMessage(int index, long time, String topic, String message, String uuid);
}
