package lew.bing.web;

import com.alibaba.dubbo.config.annotation.Reference;
import lew.bing.domain.User;
import lew.bing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by 刘国兵 on 2017/9/29.
 */

@Controller
public class ApplicationController {

    @Reference
    private UserService userService;

    @ResponseBody
    @RequestMapping("/user/{id:\\d+}")
    public User index(@PathVariable long id) {
        User user = userService.get(id);
        return user;
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login() {
        return "index";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam  String username, @RequestParam String pwd, HttpSession session){
        User user = userService.get(username);
        if (user == null) {
            return "index";
        } else {
            if (user.getPassword().equals(pwd)) {
                // session存储用户信息
                session.setAttribute("user_info",user);
                return "redirect:/user/"+user.getId();
            } else {
                return "index";
            }
        }
    }

}
