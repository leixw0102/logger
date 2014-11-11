package tv.icntv.user.value;/*
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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import redis.clients.jedis.Jedis;
import tv.icntv.logger.common.DateUtils;
import tv.icntv.logger.common.cache.IRedisCache;
import tv.icntv.logger.common.cache.Redis;
import tv.icntv.logger.common.exception.CacheExecption;
import tv.icntv.user.value.job.UserValueJob;
import tv.icntv.user.value.job.average.AverageJob;

import java.util.Map;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/08
 * Time: 11:19
 */
public class ValueJob extends Configured implements Tool {
    private String userValueFormat="user.value.day.format";
    private String userValueOutput="user.value.output.middle.path";
    private int runTool(Class<? extends Tool> toolClass,String[] args) throws Exception {
        Tool tool = ReflectionUtils.newInstance(toolClass, getConf());
        return  tool.run(args);
    }
    @Override
    public int run(String[] args) throws Exception {

        runTool(UserValueJob.class, args);

        Configuration configuration = getConf();
        final String day = DateUtils.getDay(-1).toString(configuration.get(userValueFormat));
        double maxValue=FileApi.getMax(new Path[]{new Path(configuration.get(userValueOutput)+ day)});
        configuration.setDouble("user.value.max.value",maxValue);
        System.out.println(configuration.get("outputPath")+"\t max value = "+maxValue);
        super.setConf(configuration);

        runTool(AverageJob.class, args);
        //set cache
        final Map<String,String> maps = FileApi.getMaps(new Path(configuration.get("user.value.output.path")+day));
        if(null == maps|| maps.isEmpty()){
            return 0;
        }
        Redis.execute(new IRedisCache<Boolean>() {
            @Override
            public Boolean callBack(Jedis jedis) throws CacheExecption {
                jedis.hmset(day+"-u-v-distribute",maps);
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main(String[]args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.addResource("user-value.xml");
        ToolRunner.run(configuration, new ValueJob(), args);
    }
}
