package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
}
