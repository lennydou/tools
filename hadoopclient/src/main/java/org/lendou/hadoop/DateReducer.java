package org.lendou.hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class DateReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        IntWritable result = new IntWritable(Integer.MIN_VALUE);
        values.forEach(e -> result.set(Math.max(result.get(), e.get())));

        context.write(key, result);
    }
}