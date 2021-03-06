package redis.com;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.Map;

/**
 * Hello world!
 *
 */
public class App
{
    String name;
    private final static Logger logger = LoggerFactory.getLogger(App.class);

    private Jackson2HashMapper hashMapper = new Jackson2HashMapper();

    private HashOperations hashOperations;

    public App() {
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.1.215", 6379);
        Jedis jedis = jedisPool.getResource();
        hashOperations = new HashOperations(jedis, new RedisSerializer(String.class), new RedisSerializer(Object.class));
    }

    public static void clusterTest() {
        JedisCluster jedisCluster = new JedisCluster(new HostAndPort("192.168.1.215", 7000), new JedisPoolConfig());
        jedisCluster.getClusterNodes();
        jedisCluster.set("foo", "bar");
        String foobar = jedisCluster.get("foo");

        logger.info(foobar);
        jedisCluster.rpush("nolist", "value1");
    }

    public void hashTest(){
        //JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.1.215", 6379);

        Person person = new Person();
        person.setId(Long.valueOf(19));
        person.setFirstName("tom");
        person.setLastName("lee");
        person.setGender("man");
        Map<String, Object> map = hashMapper.toHash(person);
        logger.info(map.toString());
        hashOperations.putAll("person:20", map);

        Map<String, Object> mappedHash = hashOperations.entries("person:20");
        try {
            Person person1 = (Person) hashMapper.fromHash(mappedHash);
            logger.info(person1.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }


//        try(Jedis jedis = jedisPool.getResource()) {
//            //jedis.hset("person:19", map);
//
//
//            Map<String, String> maped = jedis.hgetAll("person:19");
//            logger.info(maped.toString());
//        }
    }

    public void transactionTest(){
        GenericObjectPoolConfig poolConfig;
        String host;
        String password;
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.21.225", 6379);

        try(Jedis jedis = jedisPool.getResource()) {
            Transaction transaction = jedis.multi();

            transaction.set("name-10001", "robbie");

            Response<String> name = transaction.get("name-10001");
            transaction.exec();
            logger.info("name: " + name.get());
        }

    }
    public static void main( String[] args )
    {
        App app = new App();
        //app.transactionTest();
        //app.hashTest();
        App.clusterTest();
    }
}
