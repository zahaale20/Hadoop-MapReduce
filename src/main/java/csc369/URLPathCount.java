package csc369;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class URLPathCount {
    public static final Class OUTPUT_VALUE_CLASS = LongWritable.class;
    public static final Class OUTPUT_KEY_CLASS = Text.class;

    public static class MapperImpl extends Mapper<LongWritable, Text, Text, LongWritable> {
        private final LongWritable one = new LongWritable(1);
        private Text urlPath = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split(" ");
            Text URL = new Text();
            URL.set(line[6]);
            context.write(URL, one);
        }
    }

    public static class ReducerImpl extends Reducer<Text, LongWritable, Text, LongWritable> {
        private TreeMap<Text,LongWritable> map =new TreeMap<Text, LongWritable>();

        public void reduce(Text key, Iterable<LongWritable> values,  Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (LongWritable val : values) {
                sum += val.get();
            }
            map.put(new Text(key),new LongWritable(sum));
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