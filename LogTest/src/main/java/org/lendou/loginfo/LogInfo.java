package org.lendou.loginfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogInfo {
    private static Logger LOGGER = LoggerFactory.getLogger(LogInfo.class);

    public static void log(String logStr) {
        System.out.println("in logInfo.class");
        LOGGER.info(logStr);
    }
}
