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
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by leixw
 * <p/>
 * Author: leixw
 * Date: 2014/11/07
 * Time: 10:54
 */
public class UserValueReducer extends Reducer<Text,LongWritable,Text,DoubleWritable> {
    Double timeWeight=0.0;
    int days =30;
    double predictionRatio=1.2;
    double predictionWeight=0.3;
//    Integer payWeight=0;
//    Integer favoritesWeight=0;
//    Integer browseWeight=0;
//    Integer searchWeight=0;
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        timeWeight = configuration.getDouble("timelong.weight",0.079);
        days = configuration.getInt("user.value.from.2.end",30);
        predictionRatio = configuration.getDouble("prediction.ratio",1.2);
        predictionWeight = configuration.getDouble("prediction.weight",0.3);
    }
    private double second = 1000L;
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        Iterator<LongWritable> it=values.iterator();
        Long result = 0L;
        while (it.hasNext()){
            LongWritable v = it.next();
            result += v.get();
        }
        double r = (result /second)/days;

        DoubleWritable doubleWritable = new DoubleWritable(timeWeight*r + r * predictionRatio * predictionWeight );
        context.write(key,doubleWritable);
    }
    public static void main(String[]args){
        System.out.println(String.format("%g",(10/(double)3)));
    }
}
