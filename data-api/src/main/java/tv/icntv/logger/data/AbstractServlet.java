package tv.icntv.logger.data;/*
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

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.icntv.logger.common.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/01
 * Time: 14:12
 */
public abstract class AbstractServlet extends HttpServlet{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected Random random = new Random();
    protected String getShowType(HttpServletRequest request){
        return request.getParameter("showType");
    }
    protected final String day = DateUtils.getDay("yyyyMMdd");
    protected abstract void sendRandom(PrintWriter writer);
    protected List<String> names = Lists.newArrayList();
    protected BiMap<String,String> biMap = HashBiMap.create();
    @Override
    public void init() throws ServletException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("city-ip.txt");
        try {
            List<String> temps =  IOUtils.readLines(in);
            for(String temp:temps){
                String[] cities = temp.split("\t");
                biMap.put(cities[0],cities[1]);
                names.add(cities[0]);
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            Closeables.closeQuietly(in);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = getShowType(req);
        setJSONType(resp);
        PrintWriter writer = resp.getWriter();
        if(!Strings.isNullOrEmpty(type) && "random".equalsIgnoreCase(type)){
            sendRandom(writer);
        }else {
            sendRealData(writer);
        }
        writer.flush();
        writer.close();

    }

    public abstract void sendRealData(PrintWriter writer);

    protected void setJSONType(HttpServletResponse response){
          response.setCharacterEncoding("UTF-8");
          response.setContentType("application/json; charset=utf-8");
    }

}
