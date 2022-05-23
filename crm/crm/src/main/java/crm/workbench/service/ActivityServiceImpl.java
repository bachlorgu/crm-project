package crm.workbench.service;

import crm.workbench.domain.Activity;
import crm.workbench.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    ActivityMapper activityMapper;

    public int insertActivity(Activity activity)
    {
        return activityMapper.saveCreateActivity(activity);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String,Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int deleteActivityByIds(String[] Ids) {
        return activityMapper.deleteActivityByIds(Ids);
    }
}
