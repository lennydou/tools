package org.lendou.hadoop;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FileGen {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public static void main(String[] args) throws IOException {

        List<String> list = Lists.newArrayList();

        Calendar cal = Calendar.getInstance();
        for (int i = 1000; i <= 2000; i++) {
            list.clear();
            cal.clear();
            cal.set(Calendar.YEAR, i);
            for (int j = 0; j < 12; j++) {
                cal.set(Calendar.MONTH, j);
                int daysInMon = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int k = 1; k <= daysInMon; k++) {
                    cal.set(Calendar.DATE, k);

                    String text = String.format("%s#%d", sdf.format(cal.getTime()), RandomUtils.nextInt(0, 50) - 10);
                    list.add(text);
                }
            }

            FileUtils.writeLines(new File("/opt/www/temp/dates.txt"), list, true);
            System.out.println(i);
        }
    }
}
