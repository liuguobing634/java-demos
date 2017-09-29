package lew.bing.service.impl;

import lew.bing.dao.UserDao;
import lew.bing.domain.User;
import lew.bing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 刘国兵 on 2017/9/29.
 */
@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserServiceImpl() {
        System.out.println("user");
    }

    @Override
    @Transactional(readOnly = true)
    public User get(long id) {
        return userDao.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User get(String username) {
        return userDao.findFirstByUsername(username);
    }

    @Override
    public User register(String username, String password) {
        User user = new User(username,password);
        return userDao.save(user);
    }

    @Override
    public void destroy(User user) {
        userDao.delete(user);
    }

    @Override
    public void changePwd(User user) {
        User u = userDao.findOne(user.getId());
        if (u != null) {
            u.setPassword(user.getPassword());
            userDao.save(u);
        }
    }
}
