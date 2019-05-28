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
    //private ObjectMapper objectMapper;
    private RedisSerializer hashKeySerializer;
    private RedisSerializer hashValueSerializer;

//    HashOperations(Jedis jedis) {
//        this.jedis = jedis;
//        //objectMapper = new ObjectMapper();
//    }

    public HashOperations(Jedis jedis, RedisSerializer hashKeySerializer, RedisSerializer hashValueSerializer) {
        this.jedis = jedis;
        this.hashKeySerializer = hashKeySerializer;
        this.hashValueSerializer = hashValueSerializer;
    }

//    private String deserializeHashKey(byte[] bytes) {
////
////    }

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
        //return objectMapper.writeValueAsBytes(k);
        return hashKeySerializer.serialize(k);
    }

    private  byte[] rawHashValue(Object v) throws Exception {
        //return objectMapper.writeValueAsBytes(v);
        return hashValueSerializer.serialize(v);
    }


    private String deserializeHashKeys(byte[] bytes) {
        try {
            //return (String) objectMapper.readValue(bytes, 0, bytes.length, String.class);
            return (String) hashKeySerializer.deserialize(bytes);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new SerializationException(ex.getMessage());
        }
    }

    private Object deserializeHashValues(byte[] bytes) {
        try {
            //return (String) objectMapper.readValue(bytes, 0, bytes.length, String.class);
            return (Object) hashValueSerializer.deserialize(bytes);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new SerializationException(ex.getMessage());
        }
    }

    public Map<String, Object> entries(String key) {

        try {
            byte[] k = key.getBytes("UTF-8");

            Map<byte[], byte[]> map = jedis.hgetAll(k);

            Map<String, Object> m = new LinkedHashMap<>(map.size());

            for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
                //objectMapper.readValue(entry.getKey())
                m.put( deserializeHashKeys(entry.getKey()),deserializeHashValues(entry.getValue()));
            }
            //objectMapper.w
            return m;

        } catch ( Exception ex) {
            logger.error(ex.getMessage());
            throw new SerializationException(ex.getMessage());
        }
    }
}
