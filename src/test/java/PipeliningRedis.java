import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * Created by ozgur on 28.03.2014.
 */
public class PipeliningRedis {

    /*
    kayıt sayısı çoklanınca pipeline daha verimli çalışmakta 1 milyon kez ping atılarak kontrol edildi.l
     */

    public Jedis jedis;

    @Before
    public void prepareRedisConnection(){
        jedis=new Jedis("localhost");
    }


    @Test
    public void withoutPipelinePingTest(){
        jedis.flushAll();
        for (int i=0;i<=1000000;i++)
            jedis.ping();
    }

    @Test
    public void pipelinePingTest(){
        jedis.flushAll();
        Pipeline pipeline=jedis.pipelined();
        for (int i=0;i<=1000000;i++){
            jedis.ping();
        }
        pipeline.sync();

    }

}
