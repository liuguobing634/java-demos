package lew.bing.activemq.test;

import lew.bing.activemq.Producer;

import java.io.Serializable;

/**
 * Created by 刘国兵 on 2017/9/21.
 */
public class ProducerTest {

    public static void main(String[] args) {
        Producer producer = new Producer();
        producer.init();
        producer.sendMessage("test");

    }

}
