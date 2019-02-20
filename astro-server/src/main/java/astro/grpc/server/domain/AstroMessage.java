package astro.grpc.server.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AstroMessage {
    private String uuid;
    private String datetime;
    private int index;
    private String topic;
    private String message;
}
