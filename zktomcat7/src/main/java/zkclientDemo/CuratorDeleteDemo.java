package zkclientDemo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CuratorDeleteDemo {

    public static void main(String[] args) throws Exception {
        String path = "/zk-test";
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.107:2181")
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        client.start();
        TreeCache cache = new TreeCache(client,path);
        cache.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,"12344".getBytes());
        cache.getListenable().addListener(new TreeCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                System.out.println("节点内容为："+new String(treeCacheEvent.getData().getPath()));
            }
        });

        Thread.sleep(1000);
    }
}
