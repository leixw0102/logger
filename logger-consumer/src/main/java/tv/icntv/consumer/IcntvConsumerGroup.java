package tv.icntv.consumer;/*
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

import com.google.common.collect.Maps;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.icntv.logger.common.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/15
 * Time: 16:30
 */
public class IcntvConsumerGroup {
    private  ConsumerConnector consumer;
    private  String topic;
    private ExecutorService executor;
    private Integer thread;
    private Logger logger = LoggerFactory.getLogger(IcntvConsumerGroup.class);



    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getThread() {
        return thread;
    }

    public void setThread(Integer thread) {
        this.thread = thread;
    }
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public IcntvConsumerGroup(String topic,int thread,String groupId) {
        logger.info("init ....");
        Properties properties=null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("consumer.properties");
        } catch (IOException e) {
            logger.error("load consumer.properties error");
            return;
        }
        this.groupId = groupId;
        properties.put("group.id",this.getGroupId());
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
        this.topic = topic;

        this.thread = thread;
        executor = Executors.newFixedThreadPool(this.getThread());
        new ShutDown();

    }

    public void run(){
        logger.info("start thread ...");
        Map<String, Integer> topicCountMap = Maps.newConcurrentMap();
        topicCountMap.put(this.getTopic(),this.getThread());
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap=consumer.createMessageStreams(topicCountMap);

        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        for (final KafkaStream stream : streams) {

            executor.submit(new Consumer(stream));
        }
    }

    class ShutDown extends Thread{
        ShutDown() {
            Runtime.getRuntime().addShutdownHook(this);
        }

        @Override
        public void run() {
          logger.info("destory executors thread");
          executor.shutdown();
        }
    }

    public static void main(String[]args){
//        if(null == args || args.length !=3){
//            return;
//        }
        new IcntvConsumerGroup("icntv.no.real.time",4,"icntv-hdfs-group").run();
    }
}
