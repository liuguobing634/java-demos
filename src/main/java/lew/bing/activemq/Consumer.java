package lew.bing.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 刘国兵 on 2017/9/21.
 */
public class Consumer {


    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    private static final String BROKEN_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    AtomicInteger count = new AtomicInteger();

    //链接工厂
    ConnectionFactory connectionFactory;
    //链接对象
    Connection connection;
    //事务管理
    Session session;

    ThreadLocal<MessageConsumer> threadLocal = new ThreadLocal<>();

    public void init() {
        try {
            // 这里是普通的订阅，过往的消息无法去消费
            connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEN_URL);
            connection = connectionFactory.createConnection();
            // 要想成为持久订阅，就要设置客户端
            connection.setClientID("consumers-"+Thread.currentThread().getName());
            connection.start();
            // 事务性消息
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void getMessage(String disname) {
        try {
            // 如果是创建queue，则是点对点，而如果是创建topic，则是sub/pub
//            Queue queue = session.createQueue(disname);
            Destination destination = session.createTopic(disname);
            MessageConsumer consumer = null;
            if (threadLocal.get() != null) {
                consumer = threadLocal.get();
            } else {
                // 这是普通订阅
//                consumer = session.createConsumer(destination);
                // 这是持久订阅，在消费端出故障重启后可以对过往消息重新消费，aaa是订户名称
//                session.createConsumer(destination);
                consumer = session.createDurableSubscriber((Topic) destination, "aaa");
                threadLocal.set(consumer);
            }
            int a = (int)(Math.random() * 100);

            while (true) {
                Thread.sleep(1000);
                try {
                    TextMessage message = (TextMessage) consumer.receive();
                    if (message != null) {
                        message.acknowledge();
                        System.out.println(Thread.currentThread().getName()+": Consumer:我是消费者，我正在消费Msg"+message.getText()+"--->"+count.getAndIncrement());
                    }
                    if (count.get() == a) {
                        throw new RuntimeException(Thread.currentThread().getName() + ":有错误啦");
                    }
                    session.commit();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    session.rollback();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.session.close();
            this.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
