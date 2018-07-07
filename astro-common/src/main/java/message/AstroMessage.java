package message;

import java.io.Serializable;
import java.util.Arrays;

public class AstroMessage implements Serializable, MessageFormat {
    private String topic;
    private Integer index;
    private Long datetime;
    private String uuid;
    private String message;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AstroMessage{" + "topic='" + topic + '\'' + ", index=" + index + ", datetime=" + datetime + ", uuid='" + uuid + '\'' + ", message=" + message + '}';
    }

    @Override
    public void checkValue() {
        
    }

    @Override
    public void checkTopic() {

    }
}
