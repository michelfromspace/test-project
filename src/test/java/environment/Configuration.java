package environment;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class Configuration {
    private static String hostname;
    private static String token;
    private static final String env;

    static {
        env = Objects.requireNonNullElse(System.getProperty("test.environment"), "test");
        try (InputStream input = new FileInputStream(
                String.format("src/test/resources/%s.properties", env)
        )) {
            Properties properties = new Properties();
            properties.load(input);
            hostname = properties.getProperty("api.hostname");
            token = properties.get("api.token").toString();
        }  catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static String getHostname() {
        return hostname;
    }

    public static String getToken() {
        return token;
    }
}
