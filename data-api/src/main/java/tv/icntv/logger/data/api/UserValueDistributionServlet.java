package tv.icntv.logger.data.api;/*
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

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import redis.clients.jedis.Jedis;
import tv.icntv.logger.common.DateUtils;
import tv.icntv.logger.common.cache.IRedisCache;
import tv.icntv.logger.common.cache.Redis;
import tv.icntv.logger.common.exception.CacheExecption;
import tv.icntv.logger.data.AbstractServlet;
import tv.icntv.logger.data.domain.UserDistribute;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/03
 * Time: 09:27
 */
public class UserValueDistributionServlet extends AbstractServlet {
    @Override
    protected void sendRandom(PrintWriter writer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendRealData(PrintWriter writer) {
        final String day = DateUtils.getDay(-1).toString("yyyy-MM-dd");
        List<UserDistribute> lists=Redis.execute(new IRedisCache<List<UserDistribute>>() {
            @Override
            public List<UserDistribute> callBack(Jedis jedis) throws CacheExecption {
                String key = day + "-u-v-distribute";
                String key1 = DateUtils.getDay(-2).toString("yyyy-MM-dd");
                Map<String, String> maps = null;
                if (jedis.exists(key)) {
                    maps = jedis.hgetAll(key);
                } else if (jedis.exists(key1)) {
                    maps = jedis.hgetAll(key1);
                }
                List<UserDistribute> values = Lists.newArrayList();
                if (null == maps || maps.isEmpty()) {
                    return null;
                } else {
                    Set<String> keys = maps.keySet();
                    for (String k : keys) {
                        values.add(new UserDistribute(k, maps.get(k)));
                    }
                    return values;
                }
            }
        });
        if(null == lists || lists.isEmpty()){
            writer.println(JSON.toJSONString(new UserDistribute("error","0")));
        }else {
            writer.println(JSON.toJSONString(lists));

        }

    }
}
