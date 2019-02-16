package astro.grpc.server.domain;

import lombok.Setter;

@Setter
public class AstroMessage {
    private String uuid;
    private String datetime;
    private int index;
    private String topic;
    private String message;
}
