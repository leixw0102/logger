package tv.icntv.logger.common.jdbc;/*
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

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/13
 * Time: 10:44
 */
public class MacUtils {
    public static String macLongToString(long macAddr)
    {
        // The format of macAddr has been converted to host order by C++
        return String.format("%1$02x:%2$02x:%3$02x:%4$02x:%5$02x:%6$02x",
                (macAddr>>40)&0xff, (macAddr>>32)&0xff,
                (macAddr>>24)&0xff, (macAddr>>16)&0xff,
                (macAddr>>8) &0xff, (macAddr)&0xff );
    }

    public static void main(String[]args){
        System.out.println(macLongToString(22786551294006L));
    }
}
