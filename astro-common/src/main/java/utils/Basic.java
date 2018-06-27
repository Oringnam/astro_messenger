package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Basic {
    public static String getIp() {
        /*try {
            InetAddress local = InetAddress.getLocalHost();
            return local.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }*/
        return AstroProperties.getProperty("server.host");
    }
}
