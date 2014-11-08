package tv.icntv.user.value.job;/*
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
import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.joda.time.DateTime;
import tv.icntv.logger.common.DateUtils;
import org.apache.hadoop.io.Text;

import java.util.List;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/07
 * Time: 09:16
 */
public class UserValueJob extends Configured implements Tool{
    private String userValueFormat="user.value.day.format";
    private String userValueMonth="user.value.before.month";
    private String userValueInput="user.value.input.path";
    private String userValueOutput="user.value.output.middle.path";
    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = super.getConf();
        Job job = Job.getInstance(configuration,"user current value");
        job.setJarByClass(this.getClass());
        job.setMapperClass(UserValueMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setReducerClass(UserValueReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        FileInputFormat.setInputPaths(job, getInput(configuration));
        FileOutputFormat.setOutputPath(job,new Path(configuration.get(userValueOutput)+DateUtils.getDay(-1).toString(configuration.get(userValueFormat))));
        configuration.set("outputPath",configuration.get(userValueOutput)+DateUtils.getDay(-1).toString(configuration.get(userValueFormat)));
        super.setConf(configuration);
        return job.waitForCompletion(true)?0:1;
    }


    public Path[] getInput(Configuration configuration){
        final String inputPath = configuration.get(userValueInput);
        String format = configuration.get(userValueFormat);
        Integer  beforeMonth = configuration.getInt(userValueMonth,-1);
        DateTime start = DateUtils.getPlusMonth(beforeMonth);
        DateTime end = DateUtils.getDay(-1);
        List<String> days = DateUtils.getDay(start,end,format);
        configuration.setInt("user.value.from.2.end",days.size());
        List<Path> paths = Lists.transform(days,new Function<String, Path>() {
            @Override
            public Path apply( java.lang.String input) {

                return new Path(inputPath+input);  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        return paths.toArray(new Path[paths.size()]);
    }
    public static void main(String []args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.addResource("user-value.xml");
        ToolRunner.run(configuration,new UserValueJob(),args);
    }
}
