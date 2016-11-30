package org.lendou.jedis;

import com.google.common.collect.Lists;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;

/**
 * 集群式访问
 *
 * 简单的说，ShardedJedis是一种帮助提高读/写并发能力的群集，群集使用一致性hash来确保一个key始终被指向相同的redis server。
 * 每个redis server被称为一个shard。
 *
 * 因为每个shard都是一个master，因此使用sharding机制会产生一些限制：不能在sharding中直接使用jedis的transactions、pipelining、pub/sub这些API，
 * 基本的原则是不能跨越shard。但jedis并没有在API的层面上禁止这些行为，但是这些行为会有不确定的结果。
 * 一种可能的方式是使用keytags来干预key的分布，当然，这需要手工的干预。
 */
public class ShardedJedisMain {

    private static void testShards() {
        // 定义 shards
        List<JedisShardInfo> shards = Lists.newArrayList();

        JedisShardInfo si = new JedisShardInfo("192.168.1.205", 6379);
        si.setPassword("bjciri");
        shards.add(si);

        si = new JedisShardInfo("192.168.1.206", 6379);
        si.setPassword("bjciri");
        shards.add(si);

        // 创建 SharedJedis
        ShardedJedis sharedJedis = new ShardedJedis(shards);
        sharedJedis.set("foo", "bar");
        sharedJedis.disconnect();
    }

    private static void testShardPool() {
        // 定义 shards
        List<JedisShardInfo> shards = Lists.newArrayList();

        JedisShardInfo si = new JedisShardInfo("192.168.1.205", 6379);
        si.setPassword("bjciri");
        shards.add(si);

        si = new JedisShardInfo("192.168.1.206", 6379);
        si.setPassword("bjciri");
        shards.add(si);

        ShardedJedisPool pool = new ShardedJedisPool(new JedisPoolConfig(), shards);
        ShardedJedis jedis = pool.getResource();
        jedis.set("foo", "bar");
        // do your jobs
        // ...
        jedis.close();

        // A few moments later
        jedis = pool.getResource();
        jedis.set("foo", "bar2");
        // 获得 ShardInfo 信息
        // ShardInfo si = jedis.getShardInfo("key");
        // si.getHost/getPort/getPassword/getTimeout/si.getName
        jedis.close();

        // Finally
        pool.destroy();
    }

    private static void testKeyTag() {
        // 定义 shards
        List<JedisShardInfo> shards = Lists.newArrayList();

        JedisShardInfo si = new JedisShardInfo("192.168.1.205", 6379);
        si.setPassword("bjciri");
        shards.add(si);

        si = new JedisShardInfo("192.168.1.206", 6379);
        si.setPassword("bjciri");
        shards.add(si);

        // 通过 keytags 来确保 key 位于相同的 shard
        /**
         * The default pattern used for extracting a key tag. The pattern must have a group (between
         * parenthesis), which delimits the tag to be hashed. A null pattern avoids applying the regular
         * expression for each lookup, improving performance a little bit is key tags aren't being used.
         */
        /**
         * 其实可以这样理解, 在对key做哈希选择shard时, 使用一种规则(比如Pattern);
         * 然后真的存到redis时, 再使用真正的key.
         *
         * 这个哈希的一种方式叫做 keyTagPattern
         */
        ShardedJedis jedis = new ShardedJedis(shards, ShardedJedis.DEFAULT_KEY_TAG_PATTERN);
    }
}