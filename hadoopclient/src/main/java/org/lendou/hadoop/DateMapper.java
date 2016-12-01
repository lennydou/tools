package org.lendou.hadoop;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class DateMapper extends Mapper<Object, Text, Text, IntWritable> {


    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // 获得年份
        if (value.getLength() < 4) {
            return;
        }

        String line = value.toString();
        String year = line.substring(0, 4);
        int temp = NumberUtils.toInt(line.substring(line.indexOf("#") + 1), 0);

        context.write(new Text(year), new IntWritable(temp));
    }
}
