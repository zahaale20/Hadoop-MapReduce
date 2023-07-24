package csc369;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class URLClientRequestCount {
    public static final Class OUTPUT_VALUE_CLASS = LongWritable.class;
    public static final Class OUTPUT_KEY_CLASS = Text.class;

    public static class MapperImpl extends Mapper<LongWritable, Text, Text, LongWritable> {
        private final LongWritable one = new LongWritable(1);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            Text url2 = new Text();
            url2.set("/favicon.ico"); // hardcoded hostname/IP address
            String[] line = value.toString().split(" ");
            Text url1 = new Text();
            url1.set(line[6]);
            if(url1.equals(url2)){
                Text client = new Text();
                client.set(line[0]);
                context.write(client, one);
            }
        }
    }

    public static class ReducerImpl extends Reducer<Text, LongWritable, Text, LongWritable> {
        private LongWritable result = new LongWritable();
        private TreeMap<Text,LongWritable> map =new TreeMap<Text, LongWritable>();

        @Override
        protected void reduce(Text client, Iterable<LongWritable> counts, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable count : counts) {
                sum += count.get();
            }
            result.set(sum);
            map.put(new Text(client), new LongWritable(sum));
        }

        @Override
        protected void cleanup(Reducer.Context context) throws IOException, InterruptedException {
            List<Map.Entry<Text, LongWritable>> list = new ArrayList<Map.Entry<Text, LongWritable>>(map.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<Text, LongWritable>>()
            {
                public int compare( Map.Entry<Text, LongWritable> value1, Map.Entry<Text, LongWritable> value2 ) {
                    return (value1.getValue()).compareTo(value2.getValue() );
                }
            });
            for(Map.Entry<Text, LongWritable> entry:list){

                context.write(entry.getKey(),entry.getValue());
            }

        }
    }
}