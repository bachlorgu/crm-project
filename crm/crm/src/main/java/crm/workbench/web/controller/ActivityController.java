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
import java.util.*;

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
            ret.setMessage("添加失败(异常)");
        }

        return ret;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        List<Activity> activityList=activityService.queryActivityByConditionForPage(map);
        int totalRows=activityService.queryCountOfActivityByCondition(map);
        Map<String,Object> retmap=new HashMap<>();
        retmap.put("activityList",activityList);
        retmap.put("totalRows",totalRows);
        return retmap;
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds")
    @ResponseBody
    public Object deleteActivityByIds(String[] Ids){
        ReturnObject retobj=new ReturnObject();
        try{
            int ret = activityService.deleteActivityByIds(Ids);
            if(ret>0){
                retobj.setCode(Contant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                retobj.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
                retobj.setMessage("删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            retobj.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
            retobj.setMessage("删除失败(异常)");
        }
        return retobj;
    }

    @RequestMapping("/workbench/activity/queryActivityById")
    @ResponseBody
    public Object queryActivityById(String id){
        Activity activity=(Activity)activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/saveActivity")
    @ResponseBody
    public Object saveActivity(Activity activity,HttpSession session){
        User user=(User)session.getAttribute(Contant.SESSION_USER);
        ReturnObject ret=new ReturnObject();
        activity.setEditTime(DateUtil.timeFormat1(new Date()));
        activity.setEditBy(user.getId());
        int count=activityService.saveActivity(activity);
        try{
            if(count>0){
                ret.setCode(Contant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                ret.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
                ret.setMessage("修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            ret.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
            ret.setMessage("修改失败(异常)");
        }
        return ret;
    }

}
