package message;

import javax.script.ScriptContext;
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
       public boolean validator(String value) throws Exception {
        if(value == null) {
            throw new Exception();
        }

        return true;
    }

//    @Override
    public boolean makeMessage(int index, long time, String topic, String message, String uuid) {
        setIndex(index);
        setDatetime(time);

        try {
            setTopic(topic);
            validator(getTopic());
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            setMessage(message);
            validator(getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        setUuid(uuid);

        return true;
    }
}
