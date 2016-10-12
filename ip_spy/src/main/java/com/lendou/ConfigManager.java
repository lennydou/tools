package com.lendou;

import org.apache.commons.lang3.Validate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件管理器
 */
public class ConfigManager {

    // 配置文件所在目录
    private static String CONFIG_BASE = "/etc/ciri/config/";

    // 配置文件列表
    private static String[] configFiles = {"access.properties", "mns.properties", "mysql.properties", "oss.properties", "cache.properties"};

    // 存放配置文件的 Properties 实例
    private static final Properties props;

    static {
        props = new Properties();
        try {
            for (String configFileName : configFiles) {
                String filePath = CONFIG_BASE + configFileName;
                props.load(new FileInputStream(filePath));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得配置项的值
     *
     * @param key 配置项名称
     * @return 配置项的值
     */
    public static String get(String key) {
        return props.getProperty(key);
    }

    /**
     * 设置配置项的值
     *
     * @param key 键值
     * @param value 值
     */
    public static void set(String key, String value) {
        Validate.notEmpty(key);
        Validate.notEmpty(value);

        props.setProperty(key, value);
    }
}