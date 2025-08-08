package doanh.io.account_service.config;
import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    public static void init() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
    }
}