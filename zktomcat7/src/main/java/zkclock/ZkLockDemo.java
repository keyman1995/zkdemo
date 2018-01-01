package zkclock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZkLockDemo implements Lock {

    private static final String ZOOKEEPER_IP_REPORT = "192.168.1.107:2181";
    private static final String LOCK_PATH = "/LOCK";
    private static ZkClient client = new ZkClient(ZOOKEEPER_IP_REPORT,1000);
    private CountDownLatch cdl;
    private String beforePath;
    private String curretnPath;

    public void lock() {
        if(!tryLock()){
            waitForLock();
            lock();
        }
    }

    public ZkLockDemo(){
        if(!this.client.exists(LOCK_PATH)){
            this.client.createPersistent(LOCK_PATH);
        }
    }


    public void lockInterruptibly() throws InterruptedException {

    }

    public void waitForLock(){
        IZkDataListener listener = new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {
                if(cdl!=null){
                    cdl.countDown();
                }
            }

            public void handleDataDeleted(String s) throws Exception {
            }
        };

        this.client.subscribeDataChanges(beforePath,listener);
        if(this.client.exists(beforePath)){
            cdl = new CountDownLatch(1);
            try{
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.client.unsubscribeDataChanges(beforePath,listener);
    }

    public boolean tryLock() {

        if(curretnPath==null || curretnPath.length()<=0){
            curretnPath = this.client.createEphemeralSequential(LOCK_PATH+"/","lock");
        }
        List<String> childrens = this.client.getChildren(LOCK_PATH);
        Collections.sort(childrens);

        if(curretnPath.equals(LOCK_PATH+"/"+childrens.get(0))){
            return true;
        }else{
            int wz = Collections.binarySearch(childrens,curretnPath.substring(6));
            beforePath = LOCK_PATH+"/"+childrens.get(wz-1);
        }
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock() {
        client.delete(curretnPath);
    }

    public Condition newCondition() {
        return null;
    }
}
