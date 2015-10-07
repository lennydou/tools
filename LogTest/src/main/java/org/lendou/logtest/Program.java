package org.lendou.logtest;

import org.lendou.loginfo.LogInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Program {
    private static final Logger LOGGER = LoggerFactory.getLogger(Program.class);

    public static void main(String[] args) {
        System.out.println("Hello World");
        LOGGER.info("hello world in Program");

        LogInfo.log("in logInfo");
    }
}
