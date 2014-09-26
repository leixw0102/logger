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
import tv.icntv.logger.msg.send.ISender;
import tv.icntv.logger.msg.send.KafkaClient;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/26
 * Time: 11:04
 */
public abstract class AbstractReceiverAndSender implements IReceiverSender<LogEntry,String> {
    private Logger logger = LoggerFactory.getLogger(getClass());
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
        final List<String> msgs=msgChange(message);

        Future<Integer> future=getClient().start(new Callable() {
            @Override
            public Object call() throws Exception {
                //TODO send msgs
//                sender.send(msgs);
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        try {
            int result = future.get(20, TimeUnit.SECONDS);
            return result==0?true:false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("future result error={}",e.getMessage());
            //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();
            logger.error("future result error={}",e.getMessage());
            //To change body of catch statement use File | Settings | File Templates.
        } catch (TimeoutException e) {
            logger.error("future result error={}",e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }
    public abstract List<String> msgChange(List<com.facebook.generate.LogEntry> msgs);

    public void setClient(KafkaClient client) {
        this.client = client;
    }
}
