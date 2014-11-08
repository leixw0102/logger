package tv.icntv.user.value.job.average;/*
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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/08
 * Time: 12:00
 */
public class AverageMapper extends Mapper<LongWritable,Text,Text,IntWritable> {
    double maxValue=0.0;
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        maxValue=configuration.getDouble("user.value.max.value",0.0);
    }



    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] v = value.toString().split("\t");
        double result = (Double.parseDouble(v[1])/maxValue)*100 ;
        context.write(new Text(get((int) result)),new IntWritable(1));
    }

    private String get(int r){

        if(r>=80){
            return "80以上";
        }

        if(r>=70){
            return "70";
        }

        if(r>=60){
            return "60";
        }

        if(r>=50){
            return "50";
        }

        if(r>=40){
             return "40";
        }
        if(r>30){
            return "30";
        }
        if(r>=20){
            return "20";
        }
        if(r>=10){
            return "10";
        }
        return "10以内";
    }
}
