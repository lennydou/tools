package org.lendou.jedis;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;
import java.util.Set;

/**
 * 如果希望一次发送一批redis命令，一种有效的方式是使用pipeline
 */
public class PipelineMain {

    public static void main(String[] args) throws IOException {
        Jedis jedis = new Jedis(Constants.ip, Constants.port);
        jedis.set("foo", "bar");
        System.out.println(StringUtils.equals(jedis.get("foo"), "bar"));

        Pipeline pipeline = jedis.pipelined();
        pipeline.set("foo1", "bar1");
        pipeline.set("foo2", "bar2");
        pipeline.zadd("zfoo", 1, "abar");
        pipeline.zadd("zfoo", 2, "bbar");
        pipeline.zadd("zfoo", 3, "cbar");
        pipeline.sync();

        System.out.println(StringUtils.equals(jedis.get("foo1"), "bar1"));
        System.out.println(StringUtils.equals(jedis.get("foo2"), "bar2"));

        Response<String> pipeStr = pipeline.get("foo1");
        Response<Set<String>> sose = pipeline.zrange("zfoo", 0, -1);
        pipeline.close();

        System.out.println("pipeStr - " + pipeStr.get());
        System.out.println("soseSize - " + sose.get().size());
        if (sose.get().size() > 0) {
            for (String item : sose.get()) {
                System.out.println(item);
            }
        }

        pipeline.close();
        jedis.close();
    }
}
