package lew.bing.web;

import lew.bing.domain.User;
import lew.bing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 刘国兵 on 2017/9/29.
 */

@Controller
public class ApplicationController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/user/{id:\\d+}")
    public User index(@PathVariable long id) {
        User user = userService.get(id);
        return user;
    }

}
