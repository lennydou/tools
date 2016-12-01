package org.lendou.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class SimpleWatcher implements Watcher {

    public void process(WatchedEvent event) {
        System.out.println("Receive watched event " + event);
        System.out.println("state: " + event.getState());
        System.out.println("type: " + event.getType());
        System.out.println("path: " + event.getPath());
    }
}