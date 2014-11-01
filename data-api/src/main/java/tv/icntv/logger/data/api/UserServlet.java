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

import redis.clients.jedis.Jedis;
import tv.icntv.logger.common.DateUtils;
import tv.icntv.logger.common.cache.IRedisCache;
import tv.icntv.logger.common.cache.Redis;
import tv.icntv.logger.common.exception.CacheExecption;
import tv.icntv.logger.data.AbstractServlet;
import tv.icntv.logger.data.domain.ShowUsers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/01
 * Time: 14:02
 */
public class UserServlet extends AbstractServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        setJSONType(response);
        final String day = DateUtils.getDay("yyyy-MM-dd");
        String number = "1";
        try {
            number= Redis.execute(new IRedisCache<String>() {
                @Override
                public String callBack(Jedis jedis) throws CacheExecption {
                    if(jedis.exists(day)){
                        return jedis.get(day);
                    }
                    return "10";  //To change body of implemented methods use File | Settings | File Templates.
                }
            });
        }catch (Exception e){
             logger.error("execute redis error ! ",e);
        }

        PrintWriter writer = response.getWriter();
        writer.println(new ShowUsers(number));
        writer.flush();
        writer.close();
    }
}
