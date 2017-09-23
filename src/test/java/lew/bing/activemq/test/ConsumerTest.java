package lew.bing.activemq.test;

import lew.bing.activemq.Consumer;
import lew.bing.activemq.Producer;

/**
 * Created by 刘国兵 on 2017/9/21.
 */
public class ConsumerTest {

    public static void main(String[] args) {
        new Thread(new ConsumerRun()).start();
        new Thread(new ConsumerRun()).start();
        new Thread(new ConsumerRun()).start();
        new Thread(new ConsumerRun()).start();
        new Thread(new ConsumerRun()).start();
    }

    public static class ConsumerRun implements Runnable {

        @Override
        public void run() {
            Consumer consumer = new Consumer();
            consumer.init();
            consumer.getMessage("test");
        }
    }

}
