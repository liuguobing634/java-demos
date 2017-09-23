package lew.bing.activemq.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Date;

/**
 * Created by 刘国兵 on 2017/9/22.
 */
@Component
public class MyService {


    private static Logger logger = LoggerFactory.getLogger(MyService.class);

    @Autowired
    private JmsTemplate jmsTemplate;


    @JmsListener(destination = "aaa")
    public void aaaReceiveMessage(TextMessage name) {
        try {
            String msg = name.getText();
            logger.info("aaa message: {}, receive time: {}", msg, new Date(name.getJMSTimestamp()));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @JmsListener(destination = "aaa2")
    //这个什么用呢
    @SendTo("aaa")
    public Message<String> aaa2ReceiveMessageAndSentToAaa(TextMessage name) {
        try {
            String msg = name.getText();
            logger.info("aaa2 message: {}, receive time: {}", msg, new Date(name.getJMSTimestamp()));
            return MessageBuilder.withPayload(msg).build();
        } catch (JMSException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void aaa2SendMessage(final String name) {
//        logger.info("message2:{}", name.getPayload());
        jmsTemplate.convertAndSend("aaa2",name);
    }




}
