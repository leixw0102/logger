package tv.icntv.logger.common;/*
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
 * Date: 2014/11/05
 * Time: 09:34
 */
public class ThreadLocalIpUtils {
    private static ThreadLocal<String> ipLocal = new ThreadLocal<String>();
    public static void set(String ip){
        ipLocal.set(ip);
    }
    public static String get(){
        return ipLocal.get();
    }
    public static void remove() {
        ipLocal.remove();
    }
}
