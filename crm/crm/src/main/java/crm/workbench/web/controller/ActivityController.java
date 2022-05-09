package crm.workbench.web.controller;

import crm.settings.domain.User;
import crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @RequestMapping("/workbench/activity/index")
    public String index(HttpServletRequest request)
    {
        List<User> userList=userService.selectAllUsers();
        request.setAttribute("userList",userList);
        return "workbench/activity/index";
    }

}
