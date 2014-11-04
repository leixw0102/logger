/* Copyright 2013 Future TV, Inc.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package tv.icntv.logger.common.cache;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import tv.icntv.logger.common.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: xiaowu lei
 * Date: 13-11-15
 * Time: 下午1:32
 */
public class Redis {

    private static final String REDIS_IP="redis.ip";
    private static final String REDIS_PORT="redis.port";
    private static final String REDIS_PWD="redis.pwd";
    private static final String REDIS_MAXACTIVE="redis.maxactive";
    private static final String REDIS_MAXWAIT="redis.maxwait";
    private static final String REDIS_DB="redis.db";
    private Redis(){

    }
    private static Properties propertiesUtils=null;

    private static JedisPool jedisPool;
    private static synchronized void init(){
        try {
            propertiesUtils = PropertiesLoaderUtils.loadAllProperties("redis.properties");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(null == jedisPool){
            JedisPoolConfig config=new JedisPoolConfig();
            config.setMaxTotal(Ints.tryParse(propertiesUtils.getProperty(REDIS_MAXACTIVE)));
            config.setMaxWaitMillis(Longs.tryParse(propertiesUtils.getProperty(REDIS_MAXWAIT)));
            config.setTestOnBorrow(true);
            jedisPool=new JedisPool(config,
                    propertiesUtils.getProperty(REDIS_IP),
                    Ints.tryParse(propertiesUtils.getProperty(REDIS_PORT)),
                    Protocol.DEFAULT_TIMEOUT,
                    propertiesUtils.getProperty(REDIS_PWD));
        }
    }

    public static Jedis getJedis(){
        init();
        Jedis jedis= jedisPool.getResource();
        jedis.select(Ints.tryParse(propertiesUtils.getProperty(REDIS_DB)));
        return jedis;
    }

    public static void returnResource(Jedis jedis){
        if(null != jedis){
            jedisPool.returnResource(jedis);
        }
    }

    public static  <T> T execute(IRedisCache<T> cache){
        Jedis jedis=null;
        try{
            jedis= getJedis();
            return cache.callBack(jedis);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
           returnResource(jedis);
        }
    }

}
