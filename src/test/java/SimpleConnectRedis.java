import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ozgur on 27.03.2014.
 */
public class SimpleConnectRedis {
    @Test
    public void storeKeyToRedis() {
        Jedis jedis = new Jedis("localhost");
        jedis.set("key1", "özgür demirel");
        System.out.println(jedis.get("key1"));
    }

    @Test
    public void storeCounterOnRedis() {
        Jedis jedis = new Jedis("localhost");
        System.out.println(jedis.get("counter"));
        jedis.incr("counter");
        System.out.println(jedis.get("counter"));
    }

    @Test
    public void cacheSample() throws InterruptedException {
        String cacheKey = "cacheKey";
        Jedis jedis = new Jedis("localhost");
        jedis.set(cacheKey, "hafızaya alınan veri");
        jedis.expire(cacheKey, 15);
        System.out.println("TTL : " + jedis.ttl(cacheKey));
        Thread.sleep(2000);
        System.out.println("TTL : " + jedis.ttl(cacheKey));
        System.out.println("cached value :" + jedis.get(cacheKey));
        Thread.sleep(14000);
        System.out.println("key must be expired");
        System.out.println(jedis.get(cacheKey));
    }

    @Test
    public void storeTheSetOfData() {
        String cacheKey = "languages";
        Jedis jedis = new Jedis("localhost");
        jedis.sadd(cacheKey, "java", "c#", "fantom");
        System.out.println(jedis.smembers(cacheKey));
        System.out.println("-----------------------");
        System.out.println("sadd komutu duplike kayıtlara izin vermez");
        jedis.sadd(cacheKey, "java", "ruby");
        System.out.println(jedis.smembers(cacheKey));//keyin butun içeriğini basar
    }

    @Test
    public void storeTheSortedSetOfData() {
        //http://redis.io/commands/ZADD
        String key = "mostUsedLanguages";
        Jedis jedis = new Jedis("localhost");
        jedis.flushAll();
        jedis.zadd(key, 100, "java");
        Map<String, Double> scoreMembers = new HashMap<String, Double>();
        scoreMembers.put("fantom", 90d);
        scoreMembers.put("javascript", 80d);
        jedis.zadd(key, scoreMembers);
        System.out.println("Number of java users : " + jedis.zscore(key, "java"));
        System.out.println("Number of Elements : " + jedis.zcard(key));
        System.out.println("*-*-*-*-*-*-*-*-*-*-*");
        System.out.println("bottom to top şeklinde key deki butun değerleri alabiliriz :"+jedis.zrange(key,0,-1));
        System.out.println("*-*-*-*-*-*-*-*-*-*-*");
        System.out.println("top to bottom şeklinde key deki butun değerleri alabiliriz :"+jedis.zrevrange(key, 0, -1));
        Set<Tuple> elements=jedis.zrevrangeWithScores(key,0,-1);
        for (Tuple tuple : elements) {
            System.out.println(tuple.getElement() +" - "+ tuple.getScore());
        }
        //zincr komutunu kullanarak  elementin scorunu artırabiliriz..
        System.out.println("elemanın scoru : "+jedis.zscore(key,"fantom"));
        jedis.zincrby(key,1,"fantom");
        System.out.println("elemanın scorunu artırdım : "+jedis.zscore(key,"fantom"));
    }


}
