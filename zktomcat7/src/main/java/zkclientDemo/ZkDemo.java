package zkclientDemo;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.List;

public class ZkDemo {

    public static void main(String[] args) throws InterruptedException {

        String path = "/zk-test";

        ZkClient client = new ZkClient("192.168.1.107:2181",1000);
        /*client.createPersistent(path,true);*/
        client.createPersistent(path);
        client.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("路径"+s+"更新为："+o);
            }
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("路径"+s+"被删除了");
            }
        });
        client.subscribeChildChanges(path, new IZkChildListener() {

            public void handleChildChange(String s, List<String> list) throws Exception {
                System.out.println("路径"+s+"跟新了子节点");
                for(String child : list){
                    System.out.println("子节点是"+child);
                }
            }
        });

        client.writeData(path,"2222222",-1);
        Thread.sleep(1000);/*
        client.delete("/zk-test");
        Thread.sleep(1000);*/
        client.createPersistent(path+"/c1");
        Thread.sleep(1000);
        client.createPersistent(path+"/c2");
        Thread.sleep(1000);

    }
}
