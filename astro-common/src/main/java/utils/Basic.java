package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Basic {
    private static Logger logger = LoggerFactory.getLogger(Basic.class);

    public static String getHostIp() {
        return AstroProperties.getProperty("server.host");
    }

    public static String getLocalIP() {
        try {
            InetAddress local = InetAddress.getLocalHost();
            return local.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("utils.Basic.getLocalIP err : {}", e.getMessage());
            return null;
        }
    }
}
