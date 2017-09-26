package lew.bing.dubbo.service;

import lew.bing.dubbo.common.DemoService;

/**
 * Created by 刘国兵 on 2017/9/26.
 */
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        System.out.println("The name is " + name);
        return "Hello, " + name;
    }
}
