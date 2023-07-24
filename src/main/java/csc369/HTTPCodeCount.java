package csc369;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class HTTPCodeCount {
    public static final Class OUTPUT_VALUE_CLASS = LongWritable.class;
    public static final Class OUTPUT_KEY_CLASS = Text.class;

    public static class MapperImpl extends Mapper<LongWritable, Text, Text, LongWritable> {
        private final LongWritable one = new LongWritable(1);
        private Text urlPath = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split(" ");
            Text http = new Text();
            http.set(line[8]);
            System.out.println();
            context.write(http, one);
        }
    }

    public static class ReducerImpl extends Reducer<Text, LongWritable, Text, LongWritable> implements csc369.ReducerImpl {
        private LongWritable result = new LongWritable();

        protected void reduce(Text http, Iterable<LongWritable> counts, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable count : counts) {
                sum += count.get();
            }
            result.set(sum);
            context.write(http, result);
        }
    }
}