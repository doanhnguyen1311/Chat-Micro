package doanh.io.account_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration // Đánh dấu đây là một lớp cấu hình Spring
public class RedisConfig {

    /**
     * Cấu hình RedisTemplate để sử dụng JSON serialization cho các giá trị.
     * Nó sẽ tự động tuần tự hóa/giải tuần tự hóa các đối tượng Java thành/từ JSON trong Redis.
     *
     * @param connectionFactory Được tự động inject bởi Spring Boot từ cấu hình application.properties.
     * @return Một RedisTemplate đã được cấu hình.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Cấu hình serializer cho key: Sử dụng StringRedisSerializer để các key trong Redis là chuỗi dễ đọc.
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer()); // Cho các key của Hash

        // Cấu hình serializer cho value: Sử dụng GenericJackson2JsonRedisSerializer để lưu trữ đối tượng dưới dạng JSON.
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper());
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer); // Cho các value của Hash

        template.afterPropertiesSet(); // Gọi phương thức này để đảm bảo template được khởi tạo đúng cách
        return template;
    }

    @Bean
    public RedisTemplate<String, byte[]> redisByteArrayTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(RedisSerializer.byteArray());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(RedisSerializer.byteArray());

        return template;
    }


    /**
     * Cấu hình ObjectMapper để có thể tuần tự hóa/giải tuần tự hóa các đối tượng
     * chứa các kiểu dữ liệu của Java 8 Date & Time API (như LocalDateTime, Instant)
     * và để lưu trữ chúng dưới dạng chuỗi ISO 8601 thay vì timestamp số.
     *
     * @return Một ObjectMapper đã được cấu hình.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Đăng ký module để hỗ trợ các kiểu dữ liệu của Java 8 Date & Time API
        mapper.registerModule(new JavaTimeModule());
        // Tắt tính năng ghi ngày tháng dưới dạng timestamp số (mặc định của Jackson)
        // Thay vào đó, nó sẽ ghi dưới dạng chuỗi ISO 8601 (ví dụ: "2025-07-09T15:30:00Z")
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}