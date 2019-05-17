package redis.com;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

/**
 * Hello world!
 *
 */
public class App
{
    String name;
    private final static Logger logger = LoggerFactory.getLogger(App.class);

    public void clusterTest() {
        JedisCluster jedisCluster = new JedisCluster(new HostAndPort("192.168.21.225", 7000), new JedisPoolConfig());
        jedisCluster.set("foo", "bar");
        String foobar = jedisCluster.get("foo");

        logger.info(foobar);
        jedisCluster.rpush("nolist", "value1");
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
        app.transactionTest();
    }
}
