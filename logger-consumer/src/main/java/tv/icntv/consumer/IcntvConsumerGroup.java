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

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.icntv.consumer.hdfs.HdfsConsumer;
import tv.icntv.consumer.storm.RedisConsumer;
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

    private String consumerType;

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

    public IcntvConsumerGroup(String topic,int thread,String groupId,String consumerType){
        this(topic, thread, groupId,true,consumerType);
    }

    public IcntvConsumerGroup(String topic,int thread,String groupId,boolean isBatch,String consumerType) {
        logger.info("init ....");
        this.consumerType = consumerType;
        Properties properties=null;
        String kafkaFile="";
        try {
            kafkaFile=System.getProperty("consumer-kafka");
            if(Strings.isNullOrEmpty(kafkaFile)){
                kafkaFile = "consumer.properties";
            }
            logger.info("kafka config path ={}",kafkaFile);
            properties = PropertiesLoaderUtils.loadAllProperties(kafkaFile);
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
        logger.info(this.getGroupId()+"\t"+this.getTopic()+"\t"+this.getThread()+"\t"+properties.getProperty("zookeeper.connect"));
        new ShutDown();

    }


    public void run(){
        logger.info("start thread ...");
        Map<String, Integer> topicCountMap = Maps.newConcurrentMap();
        topicCountMap.put(this.getTopic(),this.getThread());
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap=consumer.createMessageStreams(topicCountMap);

        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        for (final KafkaStream stream : streams) {
            Consumer consumerImpl = ConsumerType.valueOf(consumerType.toUpperCase()).getConsumer(stream);
            executor.submit(consumerImpl);
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
//        if(null == args || args.length !=4){
//            return;
//        }
//        new IcntvConsumerGroup(args[0],Integer.parseInt(args[1]),args[2],args[3]).run();
//        new IcntvConsumerGroup("icntv.real.time",4,"icntv-storm-group","STORM_CONSUMER").run();
        new IcntvConsumerGroup("icntv.real.time",4,"icntv-storm-group","STORM_CONSUMER").run();
    }

        enum ConsumerType{
        HDFS_CONSUMER {
            @Override
            public Consumer getConsumer(KafkaStream stream) {
                return new HdfsConsumer(stream);  //To change body of implemented methods use File | Settings | File Templates.
            }
        },STORM_CONSUMER {
            @Override
            public Consumer getConsumer(KafkaStream stream) {
                return new RedisConsumer(stream);  //To change body of implemented methods use File | Settings | File Templates.
            }
        },DEFAULT_CONSUMER {
                @Override
                public Consumer getConsumer(KafkaStream stream) {
                    return null;
                }
            };
        public abstract Consumer getConsumer(KafkaStream stream);
    }
}
