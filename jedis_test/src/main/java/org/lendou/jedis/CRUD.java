package org.lendou.jedis;

import redis.clients.jedis.Jedis;

import static org.lendou.jedis.Constants.ip;
import static org.lendou.jedis.Constants.port;

public class CRUD {

    public static void main(String[] args) {
        testDelete();
    }

    /**
     * set(String key, String value, String nxxx, String expx, int time)
     * key - 主键
     * value - 值
     * NX|XX - NX(当key不存在时, 才设置该值); XX(当key存在时, 设置该值)
     * EX|PX - 和后面的过期时间time搭配使用; EX表示time是秒; PX表示time是毫秒
     * time - 过期时间，单位由前面的 EX|PX决定
     */
    private static void testSet() {
        Jedis jedis = new Jedis(ip, port);
        System.out.println(jedis.set("foo", "data3", "XX", "EX", 10));
        System.out.println(jedis.exists("foo"));
        System.out.println(jedis.get("foo"));

        jedis.close();
    }

    /**
     * 删除redis中的值, 返回删除的主键个数
     */
    private static void testDelete() {
        String key = "del_key";

        Jedis jedis = new Jedis(ip, port);
        jedis.set(key, "del_val");
        long delCount = jedis.del(key);
        System.out.println("Deleted key count is " + delCount);
    }
}
