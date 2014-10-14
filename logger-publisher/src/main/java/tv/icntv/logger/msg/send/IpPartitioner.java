package tv.icntv.logger.msg.send;/*
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

import com.google.common.primitives.Longs;
import kafka.producer.Partitioner;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/14
 * Time: 15:29
 */
public class IpPartitioner implements Partitioner {
    @Override
    public int partition(Object key, int i) {
        int partition = 0;
        String stringKey = (String) key;

        partition = (int) (Longs.tryParse(stringKey) % i);

        return partition;
    }
}
