package doanh.io.authentication_service.config;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    private final String secretKey;

    public JwtConfig(Dotenv dotenv) {
        this.secretKey = dotenv.get("SECRET_KEY");
    }

    public String getSecretKey() {
        return secretKey;
    }
}
