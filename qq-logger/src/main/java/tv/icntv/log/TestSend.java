package tv.icntv.log;/*
 * Copyright 2015 Future TV, Inc.
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

import com.google.inject.Guice;
import com.google.inject.Injector;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.SchedulerListener;
import it.sauronsoftware.cron4j.TaskExecutor;
import tv.icntv.sender.HdfsModule;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2015/04/02
 * Time: 09:29
 */
public class TestSend {
    public static void main(String[]args) throws UnsupportedEncodingException {
//        Scheduler scheduler = new Scheduler();
//        scheduler.addSchedulerListener(new SchedulerListener() {
//            @Override
//            public void taskLaunching(TaskExecutor executor) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                System.out.println(",,,");
//            }
//
//            @Override
//            public void taskSucceeded(TaskExecutor executor) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                System.out.println(",,,.....");
//            }
//
//            @Override
//            public void taskFailed(TaskExecutor executor, Throwable exception) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                System.out.println(",,---------,");
//            }
//        });
//
//        scheduler.setDaemon(true);
//        scheduler.schedule("* * * * * ",new Runnable(){
//            @Override
//            public void run() {
//                //To change body of implemented methods use File | Settings | File Templates.
//                System.out.println("000000000000000000"+new Date().toLocaleString());
//            }
//        });
//        scheduler.start();
//        try {
//            Thread.sleep(200000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//        scheduler.stop();
//        System.out.println(URLEncoder.encode("|||1.0.0|qq|6c:fa:a7:47:b7:43|59.37.125.39|1428403802|0|104|AppLog|ProGatherID=tmqmgx1eb6wmuz8","utf-8"));
//        System.out.println(URLDecoder.decode("|||1.0.0|qq|6c:fa:a7:47:b7:43|59.37.125.39|1428403802|0|104|AppLog|ProGatherID=tmqmgx1eb6wmuz8","utf-8"));
        HdfsModule module = new HdfsModule(new String[]{args[0],args[1],args[2],"LZO_COMPRESS","UN_COMPRESS_NONE"});
                        Injector injector = Guice.createInjector(module);//(new String[]{"d:\\douban\\error.txt","d:\\",""}));

                        Runnable client=injector.getInstance(Runnable.class);
                        new Thread(client).start();
    }
}
