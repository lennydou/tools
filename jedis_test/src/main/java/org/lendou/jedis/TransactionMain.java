package org.lendou.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.IOException;

/**
 * 如果希望一些命令一起执行而不被干扰，可以通过transaction将命令打包到一起执行
 */
public class TransactionMain {

    public static void main(String[] args) throws IOException {
        Jedis jedis = new Jedis(Constants.ip, Constants.port);

        jedis.watch("foo1", "foo2");
        Transaction t = jedis.multi();
        t.set("foo1", "bar1");
        t.set("foo2", "bar2");
        t.exec();
        t.close();

        System.out.println(jedis.get("foo1"));
        System.out.println(jedis.get("foo2"));

        jedis.close();
    }
}
