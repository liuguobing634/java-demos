package lew.bing.activemq.test;

import lew.bing.activemq.spring.AppConfig;
import lew.bing.activemq.spring.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by 刘国兵 on 2017/9/22.
 */
public class SpringConsumerTest {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MyService bean = context.getBean(MyService.class);
        while (true){
            Thread.sleep(1000);
            bean.aaa2SendMessage("hello");
        }
    }

}
