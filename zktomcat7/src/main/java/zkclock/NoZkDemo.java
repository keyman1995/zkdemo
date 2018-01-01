package zkclock;

import java.util.concurrent.CountDownLatch;

public class NoZkDemo implements Runnable {

    private static final int num = 100;

    private static CountDownLatch downLatch = new CountDownLatch(num);

    private static OrderCreate orderCreate = new OrderCreate();

    public void run() {
        try{
            downLatch.await();
            String order = orderCreate.getOrder();
            System.out.println(System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        for(int i=0;i<num;i++){
           new Thread(new NoZkDemo()).start();
           downLatch.countDown();
        }
    }

}
