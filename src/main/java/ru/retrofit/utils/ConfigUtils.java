package ru.retrofit.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class ConfigUtils {
    Properties prop = new Properties();
    private static InputStream configFile;

    static {
        try {
            configFile = new FileInputStream("src/test/resources/application.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public String getBaseUrl() {
        String url = System.getProperty("url");
        if (url == null) {
            prop.load(configFile);
            url = prop.getProperty("url");
        }
        return url;
    }
}
