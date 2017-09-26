package lew.bing.dubbo.client;

import lew.bing.dubbo.common.DemoService;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;

/**
 * Created by 刘国兵 on 2017/9/26.
 */
public class Consumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/lew/bing/dubbo/client/spring-dubbo.xml");
        context.start();
        DemoService demoService = context.getBean("demoService", DemoService.class);
        String s = demoService.sayHello("刘国兵");
        System.out.println(s);
        try {
            Thread.sleep(1000);
            demoService.sayHello("刘国兵");
            Thread.sleep(1000);
            demoService.sayHello("刘国兵");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
