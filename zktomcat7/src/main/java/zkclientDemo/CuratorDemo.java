package zkclientDemo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CuratorDemo {

    static CountDownLatch cdl =  new CountDownLatch(2);
    static ExecutorService es = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {

        String path ="/zk-test";

        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.107:2181")
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        client.start();

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("event node: "+curatorEvent.getResultCode()+", type:"+curatorEvent.getType());
                cdl.countDown();
            }
        },es).forPath(path,"test".getBytes());


        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                System.out.println("event node: "+curatorEvent.getResultCode()+", type:"+curatorEvent.getType());
               // cdl.countDown();
            }
        }).forPath(path,"test".getBytes());

        cdl.await();
        es.shutdown();
        //client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,"1234".getBytes());
    }
}
