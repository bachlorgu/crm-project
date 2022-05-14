package crm.workbench.web.controller;

import crm.commons.contants.Contant;
import crm.commons.domain.ReturnObject;
import crm.commons.utils.DateUtil;
import crm.commons.utils.UUIDUtil;
import crm.settings.domain.User;
import crm.settings.service.UserService;
import crm.workbench.domain.Activity;
import crm.workbench.service.ActivityService;
import crm.workbench.service.ActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index")
    public String index(HttpServletRequest request)
    {
        List<User> userList=userService.selectAllUsers();
        request.setAttribute("userList",userList);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/insertActivity")
    @ResponseBody
    public  Object insertActivity(Activity activity, HttpSession session)
    {
        User user=(User) session.getAttribute(Contant.SESSION_USER);
        ReturnObject ret=new ReturnObject();
        activity.setId(UUIDUtil.getuuid());
        activity.setCreateTime(DateUtil.timeFormat1(new Date()));
        activity.setCreateBy(user.getId());
        try{
            int ok=activityService.insertActivity(activity);
            if(ok==1)
            {
                ret.setCode(Contant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                ret.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
                ret.setMessage("添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            ret.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
            ret.setMessage("添加失败");
        }

        return ret;
    }

}
