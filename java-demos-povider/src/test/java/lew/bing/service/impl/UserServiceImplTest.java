package lew.bing.service.impl;

import lew.bing.domain.User;
import lew.bing.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by 刘国兵 on 2017/9/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void get() throws Exception {
        User user = userService.get(2L);
        assertNotNull(user);
    }

    @Test
    public void get1() throws Exception {
        User user = userService.get("test2");
        assertNotNull(user);
    }

    @Test
    public void register() throws Exception {
        User user = userService.register("lgb2","aaa");
        assertNotNull(user);
    }

    @Test
    public void destroy() throws Exception {
        assertTrue(true);
    }

    @Test
    public void changePwd() throws Exception {
        assertTrue(true);
    }

}