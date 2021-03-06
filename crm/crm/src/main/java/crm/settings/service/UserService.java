package crm.settings.service;

import crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User selectUserByLoginActAndPwd(Map<String,Object> map);

    List<User> selectAllUsers();

}
