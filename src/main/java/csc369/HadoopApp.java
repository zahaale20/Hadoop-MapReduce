package csc369;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class HadoopApp {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "Lab2");
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

	if (otherArgs.length < 3) {
	    System.out.println("Expected parameters: <job class> <input dir> <output dir>");
	    System.exit(-1);

	} else if ("1".equalsIgnoreCase(otherArgs[0])) {
		job.setMapperClass(URLPathCount.MapperImpl.class);
		job.setReducerClass(URLPathCount.ReducerImpl.class);
		job.setOutputKeyClass(URLPathCount.OUTPUT_KEY_CLASS);
		job.setOutputValueClass(URLPathCount.OUTPUT_VALUE_CLASS);

	} else if ("2".equalsIgnoreCase(otherArgs[0])) {
	    job.setMapperClass(HTTPCodeCount.MapperImpl.class);
		job.setReducerClass(HTTPCodeCount.ReducerImpl.class);
	    job.setOutputKeyClass(HTTPCodeCount.OUTPUT_KEY_CLASS);
	    job.setOutputValueClass(HTTPCodeCount.OUTPUT_VALUE_CLASS);

	} else if ("3".equalsIgnoreCase(otherArgs[0])) {
		job.setMapperClass(BytesToClient.MapperImpl.class);
		job.setReducerClass(BytesToClient.ReducerImpl.class);
		job.setOutputKeyClass(BytesToClient.OUTPUT_KEY_CLASS);
		job.setOutputValueClass(BytesToClient.OUTPUT_VALUE_CLASS);

	} else if ("4".equalsIgnoreCase(otherArgs[0])) {
		job.setMapperClass(URLClientRequestCount.MapperImpl.class);
		job.setReducerClass(URLClientRequestCount.ReducerImpl.class);
		job.setOutputKeyClass(URLClientRequestCount.OUTPUT_KEY_CLASS);
		job.setOutputValueClass(URLClientRequestCount.OUTPUT_VALUE_CLASS);

	} else if ("5".equalsIgnoreCase(otherArgs[0])) {
		job.setMapperClass(DayByteCount.MapperImpl.class);
		job.setReducerClass(DayByteCount.ReducerImpl.class);
		job.setOutputKeyClass(DayByteCount.OUTPUT_KEY_CLASS);
		job.setOutputValueClass(DayByteCount.OUTPUT_VALUE_CLASS);

	} else if ("6".equalsIgnoreCase(otherArgs[0])) {
		job.setMapperClass(DayByteCount.MapperImpl.class);
		job.setReducerClass(DayByteCount.ReducerImpl.class);
		job.setOutputKeyClass(DayByteCount.OUTPUT_KEY_CLASS);
		job.setOutputValueClass(DayByteCount.OUTPUT_VALUE_CLASS);

	} else {
	    System.out.println("Unrecognized job: " + otherArgs[0]);
	    System.exit(-1);
	}

        FileInputFormat.addInputPath(job, new Path(otherArgs[1]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[2]));

        System.exit(job.waitForCompletion(true) ? 0: 1);
    }

}
