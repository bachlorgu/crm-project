package crm.workbench.service;

import crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    int insertActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    int queryCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityByIds(String[] Ids);

    Activity queryActivityById(String id);

    int saveActivity(Activity activity);

    List<Activity> queryAllActivity();

    List<Activity> queryCheckedActivityByIds(String[] ids);

    int saveActivityByList(List<Activity> activityList);

}
