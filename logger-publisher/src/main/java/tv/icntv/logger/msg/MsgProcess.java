package tv.icntv.logger.msg;/*
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

import com.facebook.generate.LogEntry;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import tv.icntv.logger.msg.receive.CategoryEnum;

import java.util.*;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/26
 * Time: 11:03
 */
public class MsgProcess extends AbstractReceiverAndSender  {
    private String split="|";
    private final String prefix="[";
    private final String suffix="]";
    private final String nullString="";
    public MsgProcess() {

    }

    @Override
    public Map<String,List<String>> msgChange(List<LogEntry> msgs) {
        Map<String,List<String>> kvs = Maps.newConcurrentMap();
        if(logger.isDebugEnabled()){
            logger.debug("scribe receive msgs size={}",msgs.size());
        }
        for(LogEntry logEntry : msgs){
            // scribe category --> kafka topic
            CategoryEnum categoryEnum =null;
            try {
                categoryEnum=CategoryEnum.valueOf(logEntry.getCategory().toUpperCase());
            } catch (Exception e){
                logger.error("scribe msg category ={};but is not icntv log defined ",logEntry.category);
                continue;
            }
            String topic=categoryEnum.getKafkaTopic();
            // msg split
            List<String> values=Splitter.on(split).trimResults().limit(7).splitToList(logEntry.getMessage());
            String contents=values.get(6);
            if(Strings.isNullOrEmpty(contents)){
                logger.info("scribe receive msg={} ,but content null",contents);
                continue;
            }
            //获取消息前缀
            String[] copyValues=new String[6];
            System.arraycopy(values.toArray(),0,copyValues,0,6);
            final String prifixMsg= Joiner.on(split).join(copyValues);
            List<String> cs=Splitter.on("`").splitToList(contents);

            if(logger.isDebugEnabled()){
                logger.debug("scribe receive msg prefix={},content split size={}",prifixMsg,cs.size());
            }
            List<String> tempCs=Lists.transform(cs,new Function<String, String>() {
                @Override
                public String apply(String input) {
                    String c= input.replace(prefix,nullString).replace(suffix,nullString);  //To change body of implemented methods use File | Settings | File Templates.
                    return prifixMsg+split+c;
                }
            });

            if(kvs.containsKey(topic)){
                List<String> value=kvs.get(topic);
                value.addAll(tempCs);
                kvs.put(topic,value);
            }else{
                kvs.put(topic,tempCs);
            }
        }
        return kvs;
    }



}
