package lew.bing.service;

import lew.bing.domain.User;

/**
 * Created by 刘国兵 on 2017/9/29.
 */
public interface UserService {

    User get(long id);

    User get(String username);

    User register(String username, String password);

    void destroy(User user);

    void changePwd(User user);

}
