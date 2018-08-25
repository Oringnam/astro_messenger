package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AstroProperties {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Properties properties;

    private String filePath = "default.properties";

    public AstroProperties(String filePath) {
        this.filePath = filePath;

        try {
            loadProperties();
        } catch (IOException e) {
            logger.warn("loading properties file ({}) is failed : {} ", filePath, e.getMessage());
            logger.info("using default value for properties");
        }
    }

    private void loadProperties() throws IOException {
        properties = new Properties();
        InputStream inputStream = AstroProperties.class.getClassLoader().getResourceAsStream(filePath);

        properties.load(inputStream);
        inputStream.close();
    }

    public String get(String filepath, String key) {
        return (String) properties.get(key);
    }
}
