package crm.workbench.web.controller;

import com.sun.deploy.net.HttpResponse;
import crm.commons.contants.Contant;
import crm.commons.domain.ReturnObject;
import crm.commons.utils.DateUtil;
import crm.commons.utils.HSSFUtil;
import crm.commons.utils.UUIDUtil;
import crm.settings.domain.User;
import crm.settings.service.UserService;
import crm.workbench.domain.Activity;
import crm.workbench.service.ActivityService;
import crm.workbench.service.ActivityServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

    @RequestMapping("/workbench/activity/exportAllActivity")
    public void exportAllActivity(HttpServletResponse response){
        List<Activity> activityList=activityService.queryAllActivity();
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");
        if(activityList!=null&&activityList.size()>0){
        Activity activity=null;
        for(int i=0;i<activityList.size();i++){
            activity=activityList.get(i);
            row= sheet.createRow(i+1);
            cell=row.createCell(0);
            cell.setCellValue(activity.getId());
            cell=row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell=row.createCell(2);
            cell.setCellValue(activity.getName());
            cell=row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell=row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell=row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell=row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell=row.createCell(7);
            cell.setCellValue(activity.getCreateTime());
            cell=row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell=row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell=row.createCell(10);
            cell.setCellValue(activity.getEditBy());
        }
    }
        try {
            // 把生成的excel文件下载到客户端
            response.setContentType("application/octet-stream;charset=UTF-8");
//            response.addHeader("Content-Disposition","attachment;filename="+fileName);
            response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
            OutputStream out=response.getOutputStream();
            wb.write(out); // 将wb对象内存中的所有市场活动数据直接转入输出流内存中
            wb.close(); // 关闭资源
            out.flush(); // 刷新资源
        } catch (Exception e) {
            e.printStackTrace();
        }
   }
    @RequestMapping("/workbench/activity/exportCheckedActivityByIds")
    public void exportCheckedActivityByIds(HttpServletResponse response,String[] ids){
        List<Activity> activityList=activityService.queryCheckedActivityByIds(ids);
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表(选中)");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");
        if(activityList!=null&&activityList.size()>0){
        Activity activity=null;
        for(int i=0;i<activityList.size();i++){
            activity=activityList.get(i);
            row= sheet.createRow(i+1);
            cell=row.createCell(0);
            cell.setCellValue(activity.getId());
            cell=row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell=row.createCell(2);
            cell.setCellValue(activity.getName());
            cell=row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell=row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell=row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell=row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell=row.createCell(7);
            cell.setCellValue(activity.getCreateTime());
            cell=row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell=row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell=row.createCell(10);
            cell.setCellValue(activity.getEditBy());
        }
    }
        try {
            // 把生成的excel文件下载到客户端
            response.setContentType("application/octet-stream;charset=UTF-8");
//            response.addHeader("Content-Disposition","attachment;filename="+fileName);
            response.addHeader("Content-Disposition","attachment;filename=activityListByIds.xls");
            OutputStream out=response.getOutputStream();
            wb.write(out); // 将wb对象内存中的所有市场活动数据直接转入输出流内存中
            wb.close(); // 关闭资源
            out.flush(); // 刷新资源
        } catch (Exception e) {
            e.printStackTrace();
        }
   }

   @RequestMapping("/workbench/activity/importActivity")
   @ResponseBody
   public Object importActivity(MultipartFile activityFile,HttpSession session){
       ReturnObject ret=new ReturnObject();
       User user=(User)session.getAttribute(Contant.SESSION_USER);
       try {
            InputStream is=activityFile.getInputStream();
            HSSFWorkbook wb=new HSSFWorkbook(is);
            HSSFSheet sheet=wb.getSheetAt(0);
            HSSFRow row=null;
            HSSFCell cell=null;
            Activity activity=null;
            List<Activity> activityList=new ArrayList();
            for(int i=1;i<=sheet.getLastRowNum();i++) {
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtil.getuuid());
                activity.setCreateTime(DateUtil.timeFormat1(new Date()));
                activity.setCreateBy(user.getId());
                activity.setOwner(user.getId());
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtil.getCellValueForStr(cell);
                    if (j == 0) {
                        activity.setName(cellValue);
                    } else if (j == 1) {
                        activity.setStartDate(cellValue);
                    } else if (j == 2) {
                        activity.setEndDate(cellValue);
                    } else if (j == 3) {
                        activity.setCost(cellValue);
                    } else if (j == 4) {
                        activity.setDescription(cellValue);
                    }
                }
                activityList.add(activity);
            }
                int count=activityService.saveActivityByList(activityList);
                ret.setCode(Contant.RETURN_OBJECT_CODE_SUCCESS);
                ret.setReturnData(count);
        }catch (Exception e){
            e.printStackTrace();
            ret.setCode(Contant.RETURN_OBJECT_CODE_FAIL);
            ret.setMessage("文件导入失败");
        }
        return ret;
   }

}
