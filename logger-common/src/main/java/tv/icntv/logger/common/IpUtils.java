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

import java.io.IOException;
import java.util.Date;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/31
 * Time: 14:03
 */
public class IpUtils {
    public static String iplongToIp(long ipaddress) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((ipaddress >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x000000FF)));
        return sb.toString();
    }

    //string ip to long
    public static long ipStrToLong(String ipaddress) {
        long[] ip = new long[4];
        int position1 = ipaddress.indexOf(".");
        int position2 = ipaddress.indexOf(".", position1 + 1);
        int position3 = ipaddress.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(ipaddress.substring(0, position1));
        ip[1] = Long.parseLong(ipaddress.substring(position1+1, position2));
        ip[2] = Long.parseLong(ipaddress.substring(position2+1, position3));
        ip[3] = Long.parseLong(ipaddress.substring(position3+1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static void main(String[]args) throws IOException {
        System.out.println(ipStrToLong("123.150.146.2"));
        System.out.println(iplongToIp(3708713472L));
//        List<String> lines = Files.readLines(new File("D:\\Query_Result.txt"), Charsets.UTF_8);
//        StringBuffer sb = new StringBuffer();
//        for(int i=0;i<lines.size();i++){
//            sb.append(lines.get(i).trim()).append("\t").append((i+1)+"").append("\r\n");
//
//        }
//        Files.write(sb.toString(),new File("D:\\city.txt"),Charsets.UTF_8);
        System.out.println(new Date().getTime());
    }
}
