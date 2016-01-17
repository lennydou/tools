package com.lendou;


import com.sun.org.apache.xpath.internal.operations.Number;
import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Program {
    private static String hashUsers = "/home/dyl/documents/data_transfer/hash_userslog";

    private static String a1 = "/home/dyl/documents/data_transfer/users_012";
    private static String a2 = "/home/dyl/documents/data_transfer/users_013";

    public static void main(String[] args) throws IOException {
        Set<Long> users1 = new HashSet<Long>();
        for (String line : FileUtils.readLines(new File(hashUsers))) {
            users1.add(NumberUtils.toLong(StringUtils.substring(line, 8)));
        }

        List<String> users2 = new ArrayList<String>();
        users2.addAll(FileUtils.readLines(new File(a1)));
        users2.addAll(FileUtils.readLines(new File(a2)));

        int count = 0;
        for (String line : users2) {
            long user = NumberUtils.toLong(line);
            if (!users1.contains(user)) {
                System.out.println(user);
                ++count;
            }
        }

        System.out.println("totalCount: " + count);
    }
}
