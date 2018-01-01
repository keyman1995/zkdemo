package zkclientDemo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class RetryPolicyDemo {

    public static void main(String[] args) {
        RetryPolicy retry = new ExponentialBackoffRetry(1000,2);
    }
}
