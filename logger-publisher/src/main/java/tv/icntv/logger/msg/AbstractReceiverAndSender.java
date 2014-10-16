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
import com.google.common.base.Charsets;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.icntv.logger.exception.ReceiveExpetion;
import tv.icntv.logger.exception.SendExpetion;
import tv.icntv.logger.msg.send.KafkaClient;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/26
 * Time: 11:04
 */
public abstract class AbstractReceiverAndSender implements IReceiverSender<LogEntry,String> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    protected KafkaClient client;

    @Override
    public boolean send(String msg) throws SendExpetion {
        return send(msg, Charsets.UTF_8);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean send(String msg, Charset charset) throws SendExpetion {
        return send(msg.getBytes(charset));  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List receive(List msgs) throws ReceiveExpetion {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public KafkaClient getClient() {
        return client;
    }

    @Override
    public boolean send(byte[] msg) throws SendExpetion {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean receiveAndSend(List<LogEntry> message) throws ReceiveExpetion {
        //To change body of implemented methods use File | Settings | File Templates.

        //message change
        if(logger.isDebugEnabled()){
            logger.debug("start transfer msg to kafka msg");
        }
        final Map<String,List<String>> msgs=msgChange(message);
        if(logger.isDebugEnabled()){
            logger.debug("kafka msg generated,start send to kafka ");
        }

        return  client.send(msgs);
    }
    public abstract Map<String,List<String>> msgChange(List<com.facebook.generate.LogEntry> msgs);

    public void setClient(KafkaClient client) {
        this.client = client;
    }
}
