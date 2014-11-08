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
import org.apache.hadoop.fs.Path;
import tv.icntv.logger.common.DateUtils;
import tv.icntv.logger.data.AbstractServlet;
import tv.icntv.logger.data.domain.UserDistribute;
import tv.icntv.logger.data.utils.FileApi;

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
    private String path="/icntv/user/value/result/";
    @Override
    protected void sendRandom(PrintWriter writer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendRealData(PrintWriter writer) {
        String day = path+ DateUtils.getDay(-1).toString("yyyy-MM-dd");
        Map<String,String> maps = FileApi.getMap(new Path(day));
        if(null ==maps || maps.isEmpty()){
            day = path + DateUtils.getDay(-2).toString("yyyy-MM-dd");
        }
        maps = FileApi.getMap(new Path(day));
        List<UserDistribute> values = Lists.newArrayList();
        if(null == maps || maps.isEmpty()){
            writer.println(JSON.toJSON(new UserDistribute("无数据","0")));
        }else {
            Set<String> keys = maps.keySet();
            for(String key:keys){
                values.add(new UserDistribute(key,maps.get(key)));
            }
            writer.println(JSON.toJSONString(values));
        }

    }
}
