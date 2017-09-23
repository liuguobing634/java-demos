package lew.bing.activemq.spring;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * Created by 刘国兵 on 2017/9/21.
 */
@Configuration
@EnableJms
@ComponentScan(basePackages = {"lew.bing.activemq.spring"})
public class AppConfig {

//    private static Logger logger = LoggerFactory.getLogger(AppConfig.class);
//
//    @Override
//    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
//        // 测试register是干嘛的
//
//        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
//        endpoint.setId("myJmsEndpoint");
//        endpoint.setDestination("aaa");
//        endpoint.setMessageListener(message -> {
//            System.out.println(message);
//            // processing
//            if (message instanceof TextMessage) {
//                try {
//                    logger.info("receive message: {}", ((TextMessage) message).getText());
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                logger.info("unknown message");
//            }
//        });
//        registrar.registerEndpoint(endpoint);
//    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("3-10");
        factory.setPubSubDomain(true);
//        factory.setDestinationResolver(new BeanFactoryDestinationResolver());

        return factory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory();
        singleConnectionFactory.setTargetConnectionFactory(new ActiveMQConnectionFactory());
        return singleConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setDefaultDestinationName("aaa2");
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }



}
