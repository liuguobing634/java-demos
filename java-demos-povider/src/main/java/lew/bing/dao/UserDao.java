package lew.bing.dao;

import lew.bing.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by 刘国兵 on 2017/9/29.
 */
public interface UserDao extends CrudRepository<User,Long>{

    User findFirstByUsername(String name);


}
