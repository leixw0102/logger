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

import com.google.inject.*;

import org.junit.After;
import org.junit.Before;
import tv.icntv.logger.msg.receive.IReceiver;
import tv.icntv.logger.msg.recieve.Receiver;


/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/24
 * Time: 14:46
 */
public abstract class Test {
    protected Injector injector=null;
    @Before
    public void init(){
        injector=Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(IReceiver.class).to(Receiver.class).in(Scopes.SINGLETON);
            }
        });
    }

    @After
    public void destory(){
        injector=null;
    }
}
