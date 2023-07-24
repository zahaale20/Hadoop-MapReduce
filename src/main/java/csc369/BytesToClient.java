package csc369;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class BytesToClient {
    public static final Class OUTPUT_VALUE_CLASS = LongWritable.class;

    private final LongWritable one = new LongWritable(1);
    public static final Class OUTPUT_KEY_CLASS = Text.class;

    public static class MapperImpl extends Mapper<LongWritable, Text, Text, LongWritable> {
        private final LongWritable byteCount = new LongWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            Text host2 = new Text();
            host2.set("194.151.73.43"); // hardcoded hostname/IP address
            String[] line = value.toString().split(" ");
            Text hostname = new Text();
            hostname.set(line[0]);
            if (hostname.equals(host2)) {
                byteCount.set(Long.parseLong(line[9]));
                context.write(hostname, byteCount);
            }
        }
    }

    public static class ReducerImpl extends Reducer<Text, LongWritable, Text, LongWritable> {
        private LongWritable result = new LongWritable();

        @Override
        protected void reduce(Text word, Iterable<LongWritable> intOne, Context context) throws IOException, InterruptedException {
            long sum = 0;
            Iterator<LongWritable> itr = intOne.iterator();

            while (itr.hasNext()) {
                sum += itr.next().get();
            }
            result.set(sum);
            context.write(word, result);
        }
    }
}