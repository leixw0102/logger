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

import org.junit.Test;
import tv.icntv.logger.common.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/26
 * Time: 09:37
 */
public class KafkaProducer extends tv.icntv.logger.test.Test{

    @Test
    public void testProperties() throws IOException {
        Properties pro = PropertiesLoaderUtils.loadAllProperties("kafka-producer.properties");
        assertNotNull(pro.getProperty("metadata.broker.list"));
        assertNotNull(pro.getProperty("producer.type"));
        assertNotNull(pro.getProperty("compression.codec"));
        assertNotNull(pro.getProperty("serializer.class"));
    }


}
