package redis.com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Jackson2HashMapper {

    private final ObjectMapper typingMapper;
    private final ObjectMapper untypedMapper;

    public Jackson2HashMapper() {
        typingMapper = new ObjectMapper();
        untypedMapper = new ObjectMapper();
    }

    public Map<byte[], byte[] > toHash(Object source) {
        JsonNode tree = typingMapper.valueToTree(source);
        return untypedMapper.convertValue(tree, Map.class);
    }

    public Object fromHash(Map<byte[], byte[]> hash) throws Exception{
        try {
            return typingMapper.treeToValue(untypedMapper.valueToTree(hash), Object.class);
        } catch (JsonProcessingException ex) {
            throw new Exception(ex.getMessage());
        }

    }
}
