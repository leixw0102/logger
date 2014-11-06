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
import com.google.common.collect.Lists;
import tv.icntv.logger.msg.receive.CategoryEnum;
import tv.icntv.logger.test.Test;

import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/14
 * Time: 14:01
 */
public class KafkaMsgTest extends Test {
    private String realTimeMsg="123456|ac:sd:dd:dw|23-sdf|2.0.0|23456|231212|[232323|1|2|sd|sd|sd|sd]";
    private String noRealTimeMsg="123456|ac:sd:dd:dw|23-sdf|2.0.0|23456|231212|[232323|1|2|sd|sd|sd|sd]`[34232|1|2|sd|sd|sd|sd]";
    @org.junit.Test
    public void testMsgChange(){
        MsgProcess process = injector.getInstance(MsgProcess.class);
        LogEntry entry = new LogEntry();
        entry.setCategory(CategoryEnum.REALTIME.toString());
        entry.setMessage(realTimeMsg);
        Map<String,List<String>> maps = process.msgChange(Lists.newArrayList(entry),"");
        assertNotNull(maps);
        assertEquals("icntv.real.time",maps.keySet().iterator().next());
        assertEquals(1,maps.get("icntv.real.time").size());
        assertEquals("123456|ac:sd:dd:dw|23-sdf|2.0.0|23456|231212|232323|1|2|sd|sd|sd|sd",maps.get("icntv.real.time").get(0));
    }
    @org.junit.Test
    public void testMsgChanges(){
        MsgProcess process = injector.getInstance(MsgProcess.class);
        LogEntry entry = new LogEntry();
        entry.setCategory(CategoryEnum.NOREALTIME.toString());
        entry.setMessage(noRealTimeMsg);
        Map<String,List<String>> maps = process.msgChange(Lists.newArrayList(entry),"");
        assertNotNull(maps);
        assertEquals("icntv.no.real.time",maps.keySet().iterator().next());
        assertEquals(2,maps.get("icntv.no.real.time").size());
        assertEquals("123456|ac:sd:dd:dw|23-sdf|2.0.0|23456|231212|232323|1|2|sd|sd|sd|sd",maps.get("icntv.no.real.time").get(0));
        assertEquals("123456|ac:sd:dd:dw|23-sdf|2.0.0|23456|231212|34232|1|2|sd|sd|sd|sd",maps.get("icntv.no.real.time").get(1));
    }
}
