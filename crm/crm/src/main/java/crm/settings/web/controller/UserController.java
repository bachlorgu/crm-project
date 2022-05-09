package crm.settings.web.controller;

import crm.commons.contants.Contant;
import crm.commons.domain.ReturnObject;
import crm.commons.utils.DateUtil;
import crm.settings.domain.User;
import crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings/qx/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/tologin")
    public String toLogin()
    {
        return "settings/qx/user/login";
    }

    @RequestMapping("/dologin")
    @ResponseBody
    public Object dologin(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        //System.out.println("----------------------");
        Map<String,Object> map=new HashMap<>();
        map.put("loginact",loginAct);
        map.put("loginpwd",loginPwd);
        User user= userService.selectUserByLoginActAndPwd(map);
        ReturnObject returnObject=new ReturnObject();
        if(user==null)
        {
            //失败
            //System.out.println("-----------1-----------");
            returnObject.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或者密码错误");
        }else{
            if(DateUtil.timeFormat1(new Date()).compareTo(user.getExpiretime())>0)
            {
                //失败：过期
                //System.out.println("-----------2-----------");
                returnObject.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if(user.getLockstate()=="0"){
                //失败：被锁定
                //System.out.println("-----------3-----------");
                returnObject.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            }else if(!user.getAllowips().contains(request.getRemoteAddr()))
            {
                //System.out.println("-----------4-----------");
                //失败：IP受限
                returnObject.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("IP受限");
            }else {
                //成功
                //System.out.println("-----------5-----------");
                returnObject.setCode(Contant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("成功");
                session.setAttribute(Contant.SESSION_USER,user);
                if("true".equals(isRemPwd))
                {
                    Cookie c1=new Cookie("username",user.getLoginact());
                    c1.setMaxAge((60*60*24*10));
                    response.addCookie(c1);
                    Cookie c2=new Cookie("password",user.getLoginpwd());
                    c2.setMaxAge((60*60*24*10));
                    response.addCookie(c2);
                }else{
                    Cookie c1=new Cookie("username","1");
                    c1.setMaxAge((0));
                    response.addCookie(c1);
                    Cookie c2=new Cookie("password","1");
                    c2.setMaxAge((0));
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;
    }

    @RequestMapping("/logout")
    public String logout(HttpServletResponse response,HttpSession session)
    {
        Cookie c1=new Cookie("username","1");
        c1.setMaxAge((0));
        response.addCookie(c1);
        Cookie c2=new Cookie("password","1");
        c2.setMaxAge((0));
        response.addCookie(c2);

        session.invalidate();
        return "redirect:/";
    }

}
