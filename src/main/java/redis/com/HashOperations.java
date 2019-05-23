package redis.com;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HashOperations {
    private final static Logger logger = LoggerFactory.getLogger(HashOperations.class);

    private Jedis jedis;
    private ObjectMapper objectMapper;

    HashOperations(Jedis jedis) {
        this.jedis = jedis;
        objectMapper = new ObjectMapper();
    }

    public void putAll(String key, Map<String, Object> m) throws SerializationException {

        try {
            byte[] k = key.getBytes("UTF-8");

            Map<byte[], byte[]> map = new LinkedHashMap<>();

            for (Map.Entry<String, Object> entry : m.entrySet() ) {
                map.put(rawHashKey(entry.getKey()), rawHashValue(entry.getValue()));

            }
            jedis.hset(k, map);

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new SerializationException(ex.getMessage());
        }


    }

    private  byte[] rawHashKey(String k) throws Exception {
        return objectMapper.writeValueAsBytes(k);
    }

    private  byte[] rawHashValue(Object v) throws Exception {
        return objectMapper.writeValueAsBytes(v);
    }

    public Map<String, Object> entries(String key) {

        try {
            byte[] k = key.getBytes("UTF-8");

            Map<byte[], byte[]> map = jedis.hgetAll(k);

            Map<String, Object> m = new LinkedHashMap<>(map.size());

            for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {

            }
            //objectMapper.w

            return m;

        } catch ( Exception ex) {
            logger.error(ex.getMessage());
            throw new SerializationException(ex.getMessage());
        }


    }
}
