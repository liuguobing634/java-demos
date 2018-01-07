package lew.bing.hessian.server;

import lew.bing.hessian.service.BasicService;

/**
 * Created by 刘国兵 on 2017/10/2.
 */
public class BasicServiceImpl implements BasicService{

    public BasicServiceImpl() {
        System.out.println("init");
    }

    @Override
    public String sayHello() {
        System.out.println("hello");
        return "hello";
    }
}
