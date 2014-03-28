import org.apache.commons.beanutils.BeanUtilsBean;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ozgur on 28.03.2014.
 * User objesi uzerinde yapılan işlemleri işledim
 */
public class UsingHashWithRedis {

    public Jedis jedis;
    public User user;

    @Test
    public void seneryoyuIslet(){
        insertUserRedis();
        loadUserFromRedis();
        updateEmail();
        loadUserFromRedis();
    }

    @Before
    public void prepareJedisDriverForConnection() {
        jedis = new Jedis("localhost");
        user = new User();
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        user.setUserName("userName");
    }

    @Test
    public void beanUtilDescribeAndPropulateUtility() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //class dan oluşturulan instance in içindeki datayı alanları almak için aşğaıdaki yöntem kullanılabilir.

        Map<String, String> describe = BeanUtilsBean.getInstance().describe(user);
        for (String s : describe.keySet()) {
            System.out.println(s + " : " + describe.get(s));
        }
        User user2 = new User();
        BeanUtilsBean.getInstance().populate(user2, describe);
        System.out.println("ayrıştırıldıktan sonra birleştirilen data");
        System.out.println(user2.getEmail());
    }

    @Test
    public void insertUserRedis() {
        Map<String, String> userProperties=new HashMap<String, String>();
        userProperties.put("userName",user.getUserName());
        userProperties.put("firstName",user.getFirstName());
        userProperties.put("lastName",user.getLastName());
        userProperties.put("email",user.getEmail());
        userProperties.put("password",user.getPassword());
        jedis.hmset("user:"+user.getUserName(),userProperties);
    }

    @Test
    public void updateEmail(){
        jedis.hset("user:"+user.getUserName(),"email","updated email");
    }

    @Test
    public void loadUserFromRedis(){
        Map<String,String> properites=jedis.hgetAll("user:"+user.getUserName());
        User user=new User();
        user.setUserName(properites.get("userName"));
        user.setFirstName(properites.get("firstName"));
        user.setLastName(properites.get("lastName"));
        user.setEmail(properites.get("email"));
        user.setPassword(properites.get("password"));
        System.out.println("redisden çekilen user : "+user.toString());
    }




}
