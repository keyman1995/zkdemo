package zkclock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

public class LockOrderCreate implements Runnable {

    private static OrderCreate order = new OrderCreate();

    private static final int NUM =100;
    private static CountDownLatch downLatch = new CountDownLatch(100);
    private  Lock lock = new ZkLockDemo();

    public void createOrder(){
        String orderCode = null;
        lock.lock();
        try{
            orderCode = order.getOrder();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        System.out.println("订单order为："+System.currentTimeMillis());

    }


    public void run() {
    try{
        downLatch.await();
    } catch (InterruptedException e) {
        e.printStackTrace();
     }
        createOrder();

    }


    public static void main(String[] args) {
        for(int i=1;i<=NUM;i++){
            new Thread(new LockOrderCreate()).start();
            downLatch.countDown();
        }
    }
}
