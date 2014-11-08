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

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/08
 * Time: 13:23
 */
public class AverageReducer extends Reducer<Text,IntWritable,Text,LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

         Iterator<IntWritable> it = values.iterator();
        long count=0L;
        while (it.hasNext()){
            count+=it.next().get();
        }
        context.write(key,new LongWritable(count));
    }
}
