package tv.icntv.consumer.hdfs.utils;/*
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

import com.google.inject.Guice;
import com.google.inject.Injector;
import tv.icntv.sender.HdfsModule;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/04
 * Time: 16:17
 */
public class SendUtils {
    public static Thread getSendThread(String ...args){
        HdfsModule hdfsModule = new HdfsModule(args);
        Injector injector = Guice.createInjector(hdfsModule);
        return new Thread(injector.getInstance(Runnable.class));
    }
}
