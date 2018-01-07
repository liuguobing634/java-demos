package lew.bing.service.local.impl;

import lew.bing.domain.User;
import lew.bing.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 刘国兵 on 2017/10/13.
 */
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
        System.out.println("init");
    }

    @Override
    public User get(long id) {
        logger.info("远程调用不可用，本地方法请求id:{}",id);
        return null;
    }

    @Override
    public User get(String username) {
        logger.info("远程调用不可用，本地方法请求username:{}",username);
        return null;
    }

    @Override
    public User register(String username, String password) {
        return null;
    }

    @Override
    public void destroy(User user) {

    }

    @Override
    public void changePwd(User user) {

    }
}
