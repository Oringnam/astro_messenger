package astro.grpc.clientsdk.config;

import utils.AstroProperties;

public class AppConfig extends AstroProperties {
    public AppConfig(String filePath) {
        super(filePath);
    }

    @Override
    public String get(String key) throws NullPointerException {
        return super.get(key);
    }
}
