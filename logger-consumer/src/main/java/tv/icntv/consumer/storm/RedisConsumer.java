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

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import kafka.consumer.KafkaStream;
import redis.clients.jedis.Jedis;
import tv.icntv.consumer.Consumer;
import tv.icntv.consumer.utils.IpUtils;
import tv.icntv.logger.common.DateUtils;
import tv.icntv.logger.common.cache.IRedisCache;
import tv.icntv.logger.common.cache.Redis;
import tv.icntv.logger.common.exception.CacheExecption;
import tv.icntv.logger.common.jdbc.JdbcUtils;

import java.util.List;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/30
 * Time: 14:17
 * <p/>
 * currentDay
 * totalDay
 */
public class RedisConsumer extends Consumer {
    public RedisConsumer(KafkaStream stream) {
        super(stream);
    }

    private boolean innerExecute(String message, Jedis jedis) {
        List<String> splitValues = Splitter.on("|").limit(10).trimResults().splitToList(message);
        String icntvId = splitValues.get(0);
        String module = splitValues.get(7);
        String action = splitValues.get(8);
        String ip = splitValues.get(4);
        Long ipLong = 0L;
        if(Strings.isNullOrEmpty(icntvId)){
            logger.error("icntvId null,this log is error");
            return false;
        }
        if (ip.contains(".")) {
            ipLong = IpUtils.ipStrToLong(ip);
        } else {
            ipLong = Longs.tryParse(ip);
        }
        if (module.equalsIgnoreCase("1")) {

            String province_id = JdbcUtils.getResultForSql("select province_id from ipbase where start<=" + ipLong + " and end >= " + ipLong);
            if (Strings.isNullOrEmpty(province_id)) {
                province_id = "-1";
            }
            String day = DateUtils.getDay("yyyyMMdd");
            Integer v = action.equals("0") ? 1 : action.equals("1") ? -1 : 0;
            String userPushKey = day + "-" + icntvId;
            String userHsetKey = day + "-user-dis";
            Long num = 0L;
            if (v == 0) {
                return false;
            }
            String lastStatus = jedis.lindex(userPushKey, 0);
            if (Strings.isNullOrEmpty(lastStatus) || "null".equalsIgnoreCase(lastStatus)) {
                lastStatus = "0";
            }else {
                lastStatus = lastStatus.split("-")[1];
            }
            if (v == 1 && 1 == Ints.tryParse(lastStatus)) {
                logger.error("error,two online ");
                return false;
            }
            if (v == -1 && -1 == Ints.tryParse(lastStatus)) {
                logger.error("error ,tow offline");
                return false;
            }
            if (v == -1 && 0 == Ints.tryParse(lastStatus)) {
                logger.error("error,fist log is offline");
                return false;
            }
            if (jedis.exists(day)) {
                num = Longs.tryParse(jedis.get(day));
            }
            if (num != 0 && num + v <= 0) {
                logger.info(" total num <0");
                return false;
            }


            String oldNum = jedis.hget(userHsetKey, province_id);

            if (Strings.isNullOrEmpty(oldNum)) {
                oldNum = "0";
            }
            logger.info("province_id={} \t day={} \t exist num = {} \t current num ={} \t ip={} \t icntvId = {} \t hset oldNum ={}", province_id, day, num, v, ip, icntvId, oldNum);
            jedis.set(day, num + v + "");
            jedis.lpush(userPushKey, ip + "-" + v);

            jedis.hset(userHsetKey, province_id, (Ints.tryParse(oldNum) + v) + "");
        }

        return false;
    }

    @Override
    public void execute(final String msg) throws Exception {
        logger.info("storm msg ={}", msg);
        Redis.execute(new IRedisCache<Boolean>() {
            @Override
            public Boolean callBack(Jedis jedis) throws CacheExecption {
                return innerExecute(msg, jedis);
            }
        });
    }

}
