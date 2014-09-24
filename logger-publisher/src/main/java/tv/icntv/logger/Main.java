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
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import tv.icntv.logger.msg.receive.*;
import tv.icntv.logger.msg.send.ISender;
import tv.icntv.logger.msg.send.KafkaClient;

import java.lang.reflect.Constructor;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/24
 * Time: 14:10
 */
public class Main {
    private static final int DEFAULT_WORKERS = 10;
    private static final int DEFAULT_PORT=14630;
    public static void main(final String[]args){
         if(args.length>2){
             System.out.println(" workers  ports");
             return;
         }

        Injector injector = Guice.createInjector(new Module() {
            Integer ports=DEFAULT_PORT,workers=DEFAULT_WORKERS;


            @Override
            public void configure(Binder binder) {
                if(args.length==1){
                    ports=Ints.tryParse(args[0]);
                } if(args.length==2){
                    ports=Ints.tryParse(args[0]);
                    workers=Ints.tryParse(args[1]);
                }
                binder.bind(Integer.class).annotatedWith(Names.named("workers")).toInstance(workers);
                binder.bind(Integer.class).annotatedWith(Names.named("port")).toInstance(ports);
                try {
                    binder.bind(IConnection.class).toConstructor(ScribeServer.class.getConstructor(Integer.class,Integer.class));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    return;
                }
                binder.bind(ISender.class).to(KafkaClient.class);
                binder.bind(IReceiverAndSender.class).to(MsgExecutor.class);
            }
        });
        IConnection receiver=injector.getInstance(IConnection.class);
        receiver.start();

    }


}
