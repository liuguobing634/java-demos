package lew.bing.hessian.client;

import com.caucho.hessian.client.HessianProxyFactory;
import lew.bing.hessian.service.BasicService;

import java.net.MalformedURLException;

/**
 * Created by 刘国兵 on 2017/10/2.
 */
public class BasicServiceClient {

    public static void main(String[] args) throws MalformedURLException {
        HessianProxyFactory factory = new HessianProxyFactory();
        BasicService o = (BasicService)factory.create(BasicService.class, "http://localhost:8484/hessian");
        System.out.println(o.sayHello());
        o = (BasicService) factory.create(BasicService.class,"http://localhost:8484/hessian2");
        System.out.println(o.sayHello());

    }

}
