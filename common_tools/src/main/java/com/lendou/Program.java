package com.lendou;


import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;

public class Program {
    public static void main(String[] args) {
        int[] data = ArrayUtils.EMPTY_INT_ARRAY;
        System.out.println(data.length);

        data = ArrayUtils.add(data, 10);
        System.out.println(data.length);
    }
}
