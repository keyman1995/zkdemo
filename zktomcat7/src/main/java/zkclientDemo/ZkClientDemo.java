package zkclientDemo;
import org.apache.zookeeper.*;

import java.io.IOException;


public class ZkClientDemo {


    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        Watcher watcher = new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                    System.out.println("监听到的信息"+watchedEvent.getState());
            }
        };
        ZooKeeper zooKeeper = new ZooKeeper("192.168.1.107:2181",5000,watcher);
        Thread.sleep(3000);
        zooKeeper.create("/zk-test","1234".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Thread.sleep(3000);
        zooKeeper.setData("/zk-test","234567".getBytes(),-1);
    }

}
