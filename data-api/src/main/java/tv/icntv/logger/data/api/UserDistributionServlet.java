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
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import org.apache.commons.io.IOUtils;
import redis.clients.jedis.Jedis;
import tv.icntv.logger.common.cache.IRedisCache;
import tv.icntv.logger.common.cache.Redis;
import tv.icntv.logger.common.exception.CacheExecption;
import tv.icntv.logger.data.AbstractServlet;
import tv.icntv.logger.data.domain.UserDistribute;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/03
 * Time: 08:18
 */
public class UserDistributionServlet extends AbstractServlet {
    final private String hSetKey = day+"-user-dis";
    @Override
    protected void sendRandom(PrintWriter writer) {
//        Map<String,Integer> maps = Maps.newHashMap();
        List<UserDistribute> list = Lists.newArrayList();
        for(String name : names){
            list.add(new UserDistribute(name, Math.abs(random.nextInt() % 10000)+""));
        }
        writer.println(JSON.toJSONString(list));
    }



    @Override
    public void sendRealData(PrintWriter writer) {
        List<UserDistribute> list = Redis.execute(new IRedisCache<List<UserDistribute>>() {
            @Override
            public List<UserDistribute> callBack(Jedis jedis) throws CacheExecption {
                Map<String,String> maps=jedis.hgetAll(hSetKey);
                Set<String> sets = maps.keySet();
                List<UserDistribute> result = Lists.newArrayList();
                for(String k : sets ){
                    result.add(new UserDistribute(biMap.inverse().get(k),maps.get(k)));
                }
                return result;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        writer.println(JSON.toJSONString(list));
    }
}
