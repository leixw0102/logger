package tv.icntv.logger.data.utils;/*
 * Copyright 2014 Future TV, Inc.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

import com.google.common.collect.Maps;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/05/26
 * Time: 10:35
 */
public class FileApi  {
    private static Configuration conf=new Configuration();


    public static synchronized Map<String,String> getMap(Path inputs) {
        Map<String,String> maps = Maps.newHashMap();
        FileSystem fileSystem=null;
        BufferedReader reader=null;
        try{
            fileSystem=FileSystem.get(conf);
//
            if(!fileSystem.exists(inputs)){
                return null;
            }
            FileStatus[] fileStatuses=fileSystem.listStatus(inputs);
            if(null == fileStatuses||fileStatuses.length==0){
                return null;
            }
            for(FileStatus status:fileStatuses){
                reader=new BufferedReader(new InputStreamReader(fileSystem.open(status.getPath()),"utf-8"));

                String line=null;
                while(null != (line=reader.readLine())){
                    String[] str = line.split("\t");
                    maps.put(str[0],str[1]);
                }
            }
            return maps;
        }catch (IOException e){
            System.out.println(e);
            e.printStackTrace();
            return null;
        }finally {
            IOUtils.closeStream(reader);
            IOUtils.closeStream(fileSystem);
        }
    }

}
