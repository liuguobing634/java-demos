package lew.bing.activemq.test;

import lew.bing.activemq.Consumer;
import lew.bing.activemq.Producer;

/**
 * Created by 刘国兵 on 2017/9/21.
 */
public class ConsumerTest {

    public static void main(String[] args) throws InterruptedException {

        Consumer consumer1 = new Consumer();
        Consumer consumer2 = new Consumer();
        Consumer consumer3 = new Consumer();

        Thread thread = new Thread(new ConsumerRun(consumer1),"thread1");
        thread.start();
        new Thread(new ConsumerRun(consumer2)).start();
        new Thread(new ConsumerRun(consumer3)).start();

        Thread.sleep(30000);
        System.out.println("客户端1关闭");
        consumer1.close(); // 会出现bug
        thread.interrupt();
        Thread.sleep(1000);
        System.out.println("客户端1重启");
        new Thread(new ConsumerRun(consumer1),"thread1").start();

    }

    public static class ConsumerRun implements Runnable {

        private Consumer consumer;

        public ConsumerRun(Consumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            consumer.init();
            consumer.getMessage("test");
        }
    }

}
