package tv.icntv.logger;/*
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.icntv.logger.msg.receive.IConnection;
import tv.icntv.logger.msg.receive.IReceiverAndSender;
import tv.icntv.logger.msg.receive.MsgExecutor;
import tv.icntv.logger.msg.receive.ScribeServer;
import tv.icntv.logger.msg.send.ISender;
import tv.icntv.logger.msg.send.KafkaClient;
import tv.icntv.logger.msg.send.KafkaSender;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/24
 * Time: 14:10
 */
public class Main {

    private static String SCRIBE_WORKER="workers";
    private static String SCRIBE_PROT="port";
    private static String KAFKA_PRODUCER_THREAD_NAME="kafkaTS";
    private static final int DEFAULT_WORKERS = 10;
    private static final int DEFAULT_PORT=14630;
    private static final int DEFAULT_KAFKA_SIZE=10;
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main( String[]args){
        OptionParser parser = new Main().new ProducerConfig().getOptionParser();
        final OptionSet optionSet = parser.parse(args);

        Injector injector = Guice.createInjector(new Module() {

            @Override
            public void configure(Binder binder) {

                binder.bind(Integer.class).annotatedWith(Names.named(SCRIBE_WORKER)).toInstance(Ints.tryParse(optionSet.valueOf(SCRIBE_WORKER).toString()));
                binder.bind(Integer.class).annotatedWith(Names.named(SCRIBE_PROT)).toInstance(Ints.tryParse(optionSet.valueOf(SCRIBE_PROT).toString()));
                binder.bind(Integer.class).annotatedWith(Names.named(KAFKA_PRODUCER_THREAD_NAME)).toInstance(Ints.tryParse(optionSet.valueOf(KAFKA_PRODUCER_THREAD_NAME).toString()));
                try {
                    binder.bind(IConnection.class).toConstructor(ScribeServer.class.getConstructor(Integer.class, Integer.class));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return;
                }
                binder.bind(ISender.class).to(KafkaSender.class);
                binder.bind(KafkaClient.class).in(Scopes.SINGLETON);
                binder.bind(IReceiverAndSender.class).to(MsgExecutor.class).in(Scopes.SINGLETON);;
            }
        });
//        IConnection receiver=injector.getInstance(IConnection.class);
//        receiver.start();
          KafkaClient client = injector.getInstance(KafkaClient.class);
          client.say();
    }

    class ProducerConfig {


        public  OptionParser getOptionParser(){

            OptionParser parser = new OptionParser();
            parser.accepts(SCRIBE_WORKER).withOptionalArg().describedAs("start scribe server workers").ofType(Integer.class).defaultsTo(DEFAULT_WORKERS);
//
            parser.accepts(SCRIBE_PROT).withRequiredArg().describedAs("scribe server listener port").ofType(Integer.class).defaultsTo(DEFAULT_PORT);

            parser.accepts(KAFKA_PRODUCER_THREAD_NAME).withOptionalArg().describedAs(" kafka producer thread size").ofType(Integer.class).defaultsTo(DEFAULT_KAFKA_SIZE);
            return parser;
        }
    }
}
