package org.lendou.jedis;

import redis.clients.jedis.JedisPubSub;

public class SubListener extends JedisPubSub {

    public void onMessage(String channel, String message) {
        System.out.println(String.format("----- onMessage %s - %s", channel, message));
    }

    public void onPMessage(String pattern, String channel, String message) {
        System.out.println(String.format("----- onPMessage %s - %s", channel, message));
    }

    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("----- onSubscribe %s - %d", channel, subscribedChannels));
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("----- onUnsubscribe %s - %d", channel, subscribedChannels));
    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        System.out.println(String.format("----- onPUnsubscribe %s - %d", pattern, subscribedChannels));
    }

    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println(String.format("----- onPSubscribe %s - %d", pattern, subscribedChannels));
    }

    public void onPong(String pattern) {
        System.out.println(String.format("----- onPong %s", pattern));
    }
}
