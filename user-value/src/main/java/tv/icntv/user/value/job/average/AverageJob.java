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
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.io.Text;
import tv.icntv.logger.common.DateUtils;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/08
 * Time: 11:35
 */
public class AverageJob extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = getConf();
//        Double configuration.getDouble("user.value.max.value");
        Job job = Job.getInstance(configuration,"average job");
        job.setJarByClass(this.getClass());
        job.setMapperClass(AverageMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        job.setReducerClass(AverageReducer.class);
        String day=DateUtils.getDay(-1).toString(configuration.get("user.value.day.format"));
        FileInputFormat.setInputPaths(job,new Path(configuration.get("user.value.output.middle.path")+day ));
        FileOutputFormat.setOutputPath(job,new Path(configuration.get("user.value.output.path")+day));
        return job.waitForCompletion(true)?0:1;
    }
    public static void main(String[]args) throws Exception {
        Configuration configuration = new Configuration() ;
        configuration.addResource("user-value.xml");
        ToolRunner.run(configuration,new AverageJob(),args);
    }
}
