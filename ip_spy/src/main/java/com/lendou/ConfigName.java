package com.lendou;

public interface ConfigName {

    // 环境信息
    String SERVICE = "com.ciri.service";

    // 阿里云访问密钥
    String ACCESS_KEY_ID = "com.ciri.aliyun.accessKeyId";
    String ACCESS_KEY_SECRET = "com.ciri.aliyun.accessKeySecret";

    // Mysql 数据库
    String MYSQL_URL = "com.ciri.mysql.url";
    String MYSQL_USERNAME="com.ciri.mysql.username";
    String MYSQL_PASSWORD = "com.ciri.mysql.password";

    // 阿里云对象存储
    String OSS_ENDPOINT = "com.ciri.oss.endpoint";
    String OSS_BUCKET_NAME = "com.ciri.oss.bucketname";
    String OSS_URL_PREFIX = "com.ciri.oss.urlprefix";

    // 阿里云消息队列
    String MNS_ENDPOINT = "com.ciri.mns.endpoint";
    String MNS_SITE_ADMIN = "com.ciri.mns.site.admin";
    String MNS_ADMIN_SITE = "com.ciri.mns.admin.site";

    // 阿里云消息队列, 写入队列和写出队列
    String MNS_QUEUE_PUT = "com.ciri.mns.put";
    String MNS_QUEUE_GET = "com.ciri.mns.get";

    // Memcached 配置信息
    String CACHE_HOST = "com.ciri.cache.host";
    String CACHE_PORT = "com.ciri.cache.port";
    String CACHE_USERNAME = "com.ciri.cache.username";
    String CACHE_PASSWORD = "com.ciri.cache.password";

    // Dao 服务配置信息
    String DAO_HOST = "com.ciri.dao.host";
    String DAO_PORT = "com.ciri.dao.port";
}
