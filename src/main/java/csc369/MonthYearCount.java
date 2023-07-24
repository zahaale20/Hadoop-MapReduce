package csc369;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MonthYearCount {
    public static final Class OUTPUT_VALUE_CLASS = LongWritable.class;
    public static final Class OUTPUT_KEY_CLASS = Text.class;

    public static class MapperImpl extends Mapper<LongWritable, Text, Text, LongWritable> {
        private final LongWritable one = new LongWritable(1);
        private Text date = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] line = value.toString().split(" ");
            String m = line[3].substring(4,7) + " " + line[3].substring(8, 12);
            date.set(m);
            context.write(date, one);
        }
    }

    public static class ReducerImpl extends Reducer<Text, LongWritable, Text, LongWritable> {
        private LongWritable result = new LongWritable();

        @Override
        protected void reduce(Text datetime, Iterable<LongWritable> counts, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable count : counts) {
                sum += count.get();
            }
            result.set(sum);
            context.write(datetime, result);
        }
    }
}