package redis.com;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Map;

public class Jackson2HashMapper {

    private final ObjectMapper typingMapper;
    private final ObjectMapper untypedMapper;

    public Jackson2HashMapper() {
        typingMapper = new ObjectMapper();
        typingMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        typingMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        typingMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        typingMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        untypedMapper = new ObjectMapper();
        //this.untypedMapper = new ObjectMapper();
        this.untypedMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        this.untypedMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public Map<String, Object > toHash(Object source) {
        JsonNode tree = typingMapper.valueToTree(source);
        return untypedMapper.convertValue(tree, Map.class);
    }

    public Object fromHash(Map<String, Object> hash) throws Exception{
        try {
            return typingMapper.treeToValue(untypedMapper.valueToTree(hash), Object.class);
        } catch (JsonProcessingException ex) {
            throw new Exception(ex.getMessage());
        }

    }
}
