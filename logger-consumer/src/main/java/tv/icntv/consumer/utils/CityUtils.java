package tv.icntv.consumer.utils;/*
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

import com.google.common.collect.HashBiMap;
import com.google.common.collect.BiMap;
import com.google.common.primitives.Ints;
import tv.icntv.logger.common.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/10/31
 * Time: 17:07
 */
public class CityUtils {
    static BiMap<String,Integer>  biMap = HashBiMap.create();
    static {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("city-ip.txt");
            Set<Object> sets=properties.keySet();
            for(Object obj: sets){
                biMap.put(obj.toString(), Ints.tryParse(properties.getProperty(obj.toString())));
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public static String getCityName(Integer id){
        return biMap.inverse().get(id);
    }

    public static Integer getCityId(String name){
        if(biMap.containsKey(name)){
            return biMap.get(name);
        }
        return -1;
    }



}
