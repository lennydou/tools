package org.lendou.jedis;

import redis.clients.jedis.Jedis;

import static org.lendou.jedis.Constants.ip;
import static org.lendou.jedis.Constants.port;

/**
 * Jedis测试
 *
 * http://redis.io/topics/cluster-spec
 */
public class Main {

    public static void main(String[] args) {
        Jedis jedis = new Jedis(ip, port);
        // System.out.println(jedis.set("foo", "data3", "XX", "EX", 10));
        System.out.println(jedis.exists("foo"));
        System.out.println(jedis.get("foo"));

        jedis.close();
    }
}
