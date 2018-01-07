package lew.bing.test;

import com.alibaba.dubbo.config.annotation.Reference;
import lew.bing.domain.User;
import lew.bing.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by 刘国兵 on 2017/10/13.
 */
@SpringBootTest
@ImportResource("classpath:dubbo.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Reference(mock = "lew.bing.service.local.impl.UserServiceImpl", actives = 3)
    private UserService userService;

    @Test
    public void test1() throws InterruptedException {
        assert userService != null;
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        List<Callable<Void>> callables = new ArrayList<>();
        for (int i = 0;i < 15;i++) {
            int finalI = i;
            callables.add(() -> {
                User user = userService.get(finalI);
                System.out.println(user);
                return null;
            });
        }
        List<Future<Void>> futures = executorService.invokeAll(callables);
        Thread.sleep(20000);
    }

}
