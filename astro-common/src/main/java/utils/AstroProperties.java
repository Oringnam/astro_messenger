package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstroProperties {
    public static String getProperty(String key) {
        Properties properties = new Properties();
        InputStream inputStream = AstroProperties.class.getClassLoader().getResourceAsStream("astro.properties");
        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (String) properties.get(key);
    }

    public static void main(String args[]) {

        String test = AstroProperties.getProperty("astro.test");

        Logger logger = LoggerFactory.getLogger(AstroProperties.class);

        logger.info("astro logger {}", test);
    }
}
