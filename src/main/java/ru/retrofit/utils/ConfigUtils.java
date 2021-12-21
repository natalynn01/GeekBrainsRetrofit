package ru.retrofit.utils;

import lombok.Data;
import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@UtilityClass
@Data
public class ConfigUtils {
    private final String PATH_TO_PROPERTIES = "src/test/resources/application.properties";
    private Properties properties = new Properties();
    private String dbUsername = System.getProperty("dbUsername");
    private String dbPassword = System.getProperty("dbPassword");

    static {
        try {
            properties.load(new FileInputStream(PATH_TO_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBaseUrl() {
        String url = System.getProperty("url");
        if (url == null) {
            url = properties.getProperty("url");
        }
        return url;
    }

    public static Properties getProperties() {
        if (!(dbUsername == null) && !(dbPassword == null)) {
            properties.setProperty("dbUsername", dbUsername);
            properties.setProperty("dbPassword", dbPassword);
        }
        return properties;
    }
}
