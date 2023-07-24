package csc369;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class DayByteCount {
    public static final Class OUTPUT_VALUE_CLASS = LongWritable.class;
    public static final Class OUTPUT_KEY_CLASS = Text.class;

    public static class MapperImpl extends Mapper<LongWritable, Text, Text, LongWritable> {
        private Text date = new Text();
        private LongWritable byteCount = new LongWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split(" ");
            String m = line[3].substring(4,7) + " " + line[3].substring(1, 3) + ", " + line[3].substring(8, 12);
            date.set(m);
            byteCount.set(Long.parseLong(line[9]));
            context.write(date, byteCount);
        }
    }

    public static class ReducerImpl extends Reducer<Text, LongWritable, Text, LongWritable> {

        private TreeMap<Text,LongWritable> map =new TreeMap<Text, LongWritable>();

        @Override
        protected void reduce(Text datetime, Iterable<LongWritable> counts, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable count : counts) {
                sum += count.get();
            }
            map.put(new Text(datetime), new LongWritable(sum));
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