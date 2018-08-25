package astro.grpc.server.config;

import utils.AstroProperties;

public class AppConfig {
    private static String filepath = "server.properties";

    public static String get(String key) {
        return AstroProperties.getProperty(filepath, key);
    }
}
