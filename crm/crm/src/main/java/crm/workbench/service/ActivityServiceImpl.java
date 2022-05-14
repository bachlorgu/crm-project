package crm.workbench.service;

import crm.workbench.domain.Activity;
import crm.workbench.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    ActivityMapper activityMapper;

    public int insertActivity(Activity activity)
    {
        return activityMapper.insert(activity);
    }

}
