package astro.grpc.server.config;

import utils.AstroProperties;

public class AppConfig extends AstroProperties {

    public AppConfig(String filePath) {
        super(filePath);
    }

    @Override
    public String get(String filepath, String key) {
        return super.get(filepath, key);
    }
}
