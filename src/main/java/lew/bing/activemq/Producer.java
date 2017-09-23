package lew.bing.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by 刘国兵 on 2017/9/20.
 */
public class Producer {

    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    private static final String BROKEN_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    AtomicInteger count = new AtomicInteger(0);

    //链接工厂
    ConnectionFactory connectionFactory;
    //链接对象
    Connection connection;
    //事务管理
    Session session;
    ThreadLocal<MessageProducer> threadLocal = new ThreadLocal<>();

    public void init() {
        try {
            connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEN_URL);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String disname) {
        try {
//            Queue queue = session.createQueue(disname);
            Destination destination = session.createTopic(disname);
            MessageProducer producer = null;
            if (threadLocal.get() != null) {
                producer = threadLocal.get();
            } else {
                producer = session.createProducer(destination);
                threadLocal.set(producer);
            }
            while (true) {
                Thread.sleep(1000);
                int num = count.getAndIncrement();
                // 发送TextMessage
                String msg = Thread.currentThread().getName()+
                        "producer:我是大帅哥，我现在正在生产东西！,count:"+num;
                TextMessage message = session.createTextMessage(msg);
                System.out.println(msg);
                producer.send(message);
                // 提交session
                session.commit();
            }
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
