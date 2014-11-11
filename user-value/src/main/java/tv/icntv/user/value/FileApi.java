package tv.icntv.user.value;/*
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
import com.google.common.primitives.Doubles;
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


    public static synchronized Double getMax(Path[] inputs) {
        FileSystem fileSystem=null;
        BufferedReader reader=null;
        try{
            fileSystem=FileSystem.get(conf);
//
            FileStatus[] fileStatuses=fileSystem.listStatus(inputs);
            if(null == fileStatuses||fileStatuses.length==0){
                System.out.println("null...");
                return 0.0;
            }
            Double result = 0.0;
            for(FileStatus status:fileStatuses){
                reader=new BufferedReader(new InputStreamReader(fileSystem.open(status.getPath()),"utf-8"));

                String line=null;
                while(null != (line=reader.readLine())){
                    String[] str = line.split("\t");
                    Double value = Double.parseDouble(str[1]);
                    if(result >=value){
                        continue;
                    }
                    result = value;
                }
            }
            return result;
        }catch (IOException e){
            System.out.println(e);
            e.printStackTrace();
            return 0.0;
        }finally {
            IOUtils.closeStream(reader);
            IOUtils.closeStream(fileSystem);
        }
    }

    public static synchronized Map<String,String> getMaps(Path path){
        FileSystem fileSystem=null;
        BufferedReader reader=null;
        Map<String,String> maps = Maps.newHashMap();
        try{
            fileSystem=FileSystem.get(conf);
//
            FileStatus[] fileStatuses=fileSystem.listStatus(path);
            if(null == fileStatuses||fileStatuses.length==0){
                System.out.println("null...");
                return null;
            }
            Double result = 0.0;
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

    public static void main(String[]args){
//        Double r=writeDat(new Path[]{new Path("/icntv/user/value/2014-11-08/")});
//        System.out.println(r);
    }

}
