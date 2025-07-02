package doanh.io.account_service.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

        public static String encode(CharSequence rawPassword) {
            return "";
        }

        public static boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encoder.matches(rawPassword, encodedPassword);
        }
}
