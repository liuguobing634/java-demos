package lew.bing.dubbo.service;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by 刘国兵 on 2017/9/26.
 */
public class Provider2 {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/lew/bing/dubbo/service/spring-dubbo(2).xml");
        context.start();
        System.out.println("提供者2，任意键退出");
        System.in.read();
    }
}
