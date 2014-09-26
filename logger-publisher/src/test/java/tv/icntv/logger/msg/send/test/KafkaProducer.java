package tv.icntv.logger.msg.send.test;/*
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

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import tv.icntv.logger.common.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/26
 * Time: 09:37
 */
public class KafkaProducer {
    void test() throws IOException {
        Properties pro = PropertiesLoaderUtils.loadAllProperties("kafka-producer.properties");
        ProducerConfig config=new ProducerConfig(pro);
        Producer<String, String> producer=new Producer<String, String>(config);
        KeyedMessage<String,String> data=new KeyedMessage<String, String>("test", "sdfsd");
        producer.send(data);
        producer.close();
        System.out.println("...");
    }
}
