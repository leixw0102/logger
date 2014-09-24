package tv.icntv.logger.msg.recieve;/*
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

import com.google.common.collect.Lists;
import tv.icntv.logger.exception.ReceiveExpetion;
import tv.icntv.logger.msg.receive.IReceiver;

import java.util.List;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/09/24
 * Time: 14:41
 */
public class Receiver implements IReceiver {
    @Override
    public List<String> receive() throws ReceiveExpetion {
        System.out.println("receive..");
        return Lists.newArrayList("hello word");  //To change body of implemented methods use File | Settings | File Templates.
    }


}
