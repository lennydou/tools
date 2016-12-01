package org.lendou.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public class Program {

    private static final String PATH = "/onebox/ciri/service/site";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZkClient client = new ZkClient("192.168.1.206:2181", 5000);

        client.subscribeDataChanges("/onebox/ciri/data", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("data change");
                System.out.println("s - " + s);
                System.out.println("o - " + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("data deleted");
                System.out.println("s - " + s);
            }
        });

        Thread.sleep(5000);
        client.writeData("/onebox/ciri/data", "I am the changed data");

        Thread.sleep(5000);
        client.delete("/onebox/ciri/data");

        Thread.sleep(100000);
        client.close();
    }
}
