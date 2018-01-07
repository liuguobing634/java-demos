package lew.bing.hessian.server;

import com.caucho.hessian.server.HessianServlet;
import lew.bing.hessian.service.BasicService;

import javax.servlet.annotation.WebServlet;

/**
 * Created by 刘国兵 on 2017/10/2.
 */
@WebServlet(urlPatterns = "/another")
public class AnotherBasicServiceImpl extends HessianServlet implements BasicService {



    @Override
    public String sayHello() {
        return "mmb";
    }
}
