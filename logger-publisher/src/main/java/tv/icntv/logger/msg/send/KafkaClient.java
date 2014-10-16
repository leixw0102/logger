package tv.icntv.logger.msg.send;/*
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
import com.google.inject.Inject;
import com.google.inject.name.Named;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.icntv.logger.common.PropertiesLoaderUtils;
import tv.icntv.logger.exception.SendExpetion;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/24
 * Time: 15:29
 */
public class KafkaClient {
    @Inject
    @Named("kafkaTS")
    private int kafkaThreadSize ;
    private Logger logger = LoggerFactory.getLogger(KafkaClient.class);
    ProducerConfig producerConfig ;
    Producer<String,String> producer = null;
    public KafkaClient() {
        try {
            Properties pro = PropertiesLoaderUtils.loadAllProperties("kafka-producer.properties");
            producerConfig = new ProducerConfig(pro);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * key == topic
     * value == message;
     * @param kvs
     */
    public boolean send(Map<String,List<String>> kvs){

        if(null == kvs || kvs.isEmpty()){
            logger.info("send kafka msg is null");
            return true;
        }
        Iterator<String> it = kvs.keySet().iterator();
        try{
        while (it.hasNext()){
            String topic = it.next();
            send(topic,kvs.get(topic));
        }
        }catch (Exception e){
            logger.error("send kafka msg error:",e);
            return false;
        }
        return true;
    }

    public boolean send(final String topic,List<String> msgs){
        producer = new Producer<String, String>(producerConfig);
        List<KeyedMessage<String,String>> kafkaMsgs=Lists.transform(msgs,new Function<String,KeyedMessage<String,String>>() {
            @Override
            public KeyedMessage<String,String> apply( java.lang.String input) {
                String ip= Splitter.on("|").limit(6).trimResults().splitToList(input).get(4);
                return new KeyedMessage<String,String>(topic,ip,input);  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        if(logger.isDebugEnabled()){
            logger.debug("send topic ={},and size={}",topic ,kafkaMsgs.size());
        }
        producer.send(kafkaMsgs);
        producer.close();
        return true;
    }

 }
