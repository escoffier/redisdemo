package redis.com;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisSerializer<T> {
    private static final Logger logger = LoggerFactory.getLogger(RedisSerializer.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private final JavaType javaType;

    public RedisSerializer(Class<T> javaType) {
        this.javaType = getJavaType(javaType);
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }
    public T deserialize(byte[] bytes) {
        try {
            return (T) this.objectMapper.readValue(bytes, 0, bytes.length, javaType);
        } catch (Exception ex) {
            logger.error("Could not read JSON: " + ex.getMessage());
            throw new SerializationException(ex.getMessage());
        }
    }

    public byte[] serialize(Object t) {
        try {
            return  objectMapper.writeValueAsBytes(t);
        } catch (Exception ex) {
            logger.error("Could not write JSON: " + ex.getMessage());
            throw new SerializationException(ex.getMessage());
        }
    }


}
