package tv.icntv.logger.test;/*
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

import com.google.common.primitives.Ints;
import com.google.inject.*;

import com.google.inject.name.Names;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.junit.After;
import org.junit.Before;
import tv.icntv.logger.Main;
import tv.icntv.logger.msg.IReceiverSender;
import tv.icntv.logger.msg.MsgProcess;
import tv.icntv.logger.msg.ScribeServer;
import tv.icntv.logger.msg.receive.IConnection;
import tv.icntv.logger.msg.receive.IReceiver;
import tv.icntv.logger.msg.recieve.Receiver;
import tv.icntv.logger.msg.send.KafkaClient;


/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/24
 * Time: 14:46
 */
public abstract class Test {
    protected Injector injector=null;
    private static String SCRIBE_WORKER="workers";
    private static String SCRIBE_PORT="port";
    private static String KAFKA_PRODUCER_THREAD_NAME="kafkaTS";
    private static final int DEFAULT_WORKERS = 10;
    private static final int DEFAULT_PORT=14630;
    private static final int DEFAULT_KAFKA_SIZE=10;
    @Before
    public void init(){

        injector=Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(Integer.class).annotatedWith(Names.named(SCRIBE_WORKER)).toInstance(10);
                binder.bind(Integer.class).annotatedWith(Names.named(SCRIBE_PORT)).toInstance(14630);
                binder.bind(Integer.class).annotatedWith(Names.named(KAFKA_PRODUCER_THREAD_NAME)).toInstance(10);
                try {
                    binder.bind(IConnection.class).toConstructor(ScribeServer.class.getConstructor(Integer.class, Integer.class));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return;
                }
                binder.bind(KafkaClient.class).in(Scopes.SINGLETON);
                binder.bind(IReceiverSender.class).to(MsgProcess.class).in(Scopes.SINGLETON);
            }
        });
    }

    @After
    public void destory(){
        injector=null;
    }
}
