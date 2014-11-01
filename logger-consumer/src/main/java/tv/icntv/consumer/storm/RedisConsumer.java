package tv.icntv.consumer.storm;/*
 * Copyright 2014 Future TV, Inc.
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.icntv.tv/licenses/LICENSE-1.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import redis.clients.jedis.Jedis;
import tv.icntv.consumer.Consumer;
import tv.icntv.consumer.storm.user.UserCount;
import tv.icntv.logger.common.DateUtils;
import tv.icntv.logger.common.cache.IRedisCache;
import tv.icntv.logger.common.cache.Redis;
import tv.icntv.logger.common.exception.CacheExecption;

import java.util.List;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/30
 * Time: 14:17
 *
 * currentDay
 * totalDay
 */
public class RedisConsumer extends Consumer {
    public RedisConsumer(KafkaStream stream) {
        super(stream);
    }


    ILogger ilogger = new UserCount();
    @Override
    public void executeBatch(List<MessageAndMetadata<byte[], byte[]>> msgs) throws Exception {
        final List<String> lines=Lists.transform(msgs,new Function<MessageAndMetadata<byte[], byte[]>, String>() {
            @Override
            public String apply( MessageAndMetadata<byte[], byte[]> input) {
                return new String(input.message());
            }
        });
        if(null == lines || lines.isEmpty()){
            logger.info(" kafka msg 2 string ;but result null");
            return;
        }
        Redis.execute(new IRedisCache<Boolean>() {
            @Override
            public Boolean callBack(Jedis jedis) throws CacheExecption {
                for(String line : lines){
                    logger.info("msg info ={}",line);
                    List<String> splitValues=Splitter.on("|").limit(10).trimResults().splitToList(line);
                    String icntvId = splitValues.get(0);
                    String module = splitValues.get(7);
                    String action = splitValues.get(8);
                    String ip = splitValues.get(4);
                    if(module.equalsIgnoreCase("1")){
                        String key = DateUtils.getDay("yyyyMMdd")+"-"+icntvId;
                        String value = action.equals("0")?"1":action.equals("1")?"-1":"0";
                        if(jedis.exists(key)){
                            jedis.lpush(key,value);
                            //set num
                            //set hset ,ip
                            return true;
                        }
                        if(value.equalsIgnoreCase("-1") || value.equalsIgnoreCase("0")){
                            logger.error("error .. but return true");
                            return true;
                        }
                        return false;
                    }
                }
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }) ;
    }



    @Override
    public void execute(MessageAndMetadata<byte[], byte[]> msg) throws Exception {
        final String message = new String(msg.message());
        Redis.execute(new IRedisCache<Boolean>() {
            @Override
            public Boolean callBack(Jedis jedis) throws CacheExecption {
                List<String> splitValues = Splitter.on("|").limit(10).trimResults().splitToList(message);
                String icntvId = splitValues.get(0);
                String module = splitValues.get(7);
                String action = splitValues.get(8);
                if (module.equalsIgnoreCase("1")) {
                    String key = DateUtils.getDay("yyyyMMdd") + "-" + icntvId;
                    String value = action.equals("0") ? "1" : action.equals("1") ? "-1" : "0";
                    if (jedis.exists(key)) {
                        jedis.lpush(key, value);
                        return true;
                    }
                    if (value.equalsIgnoreCase("-1") || value.equalsIgnoreCase("0")) {
                        logger.error("error .. but return true");
                        return true;
                    }
                    jedis.lpush(key, value);
                    return true;
                }
                return false;
            }
        });
    }
}
