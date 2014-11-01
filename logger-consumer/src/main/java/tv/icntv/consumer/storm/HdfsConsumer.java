package tv.icntv.consumer.storm;/*
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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import tv.icntv.consumer.Consumer;
import tv.icntv.consumer.storm.utils.FileUtils;

import java.util.List;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/29
 * Time: 09:55
 */
@Deprecated
public class HdfsConsumer extends Consumer {
    FileUtils fileUtils = null;

    public HdfsConsumer(KafkaStream stream) {
        super(stream,true);
        fileUtils = new FileUtils();
    }

    @Override
    public void executeBatch(List<MessageAndMetadata<byte[], byte[]>> msgs) throws Exception {
        List<String> lines=Lists.transform(msgs,new Function<MessageAndMetadata<byte[], byte[]>, String>() {
            @Override
            public String apply( MessageAndMetadata<byte[], byte[]> input) {
                return new String(input.message());  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        String message= Joiner.on("\r\n").join(lines);
        fileUtils.write(message);
        if(logger.isDebugEnabled()){
            logger.debug("msg writed to file");
        }
        return;
    }

    @Override
    public void execute(MessageAndMetadata<byte[], byte[]> msg) throws Exception {
        fileUtils.write(new String(msg.message())+"\r\n");
        return;
    }

}
