package org.lendou.jedis;

import redis.clients.jedis.Jedis;

/**
 * 发布订阅模型
 *
 * 当 jedis.subscribe(new SubListener(), Constants.channel); 被调用的时候, 程序会被阻塞住
 * jedis.publish(channel_name, msg) 发布消息
 */
public class PubSubMain {
    public static void main(String[] args) {
        System.out.println("hello world");

        Jedis jedis = new Jedis(Constants.ip, Constants.port);
        jedis.subscribe(new SubListener(), Constants.channel);

        System.out.println("jedis goto close");
        jedis.close();

        // jedis.publish(Constants.channel, "msg");
    }
}
