package crm.commons.utils;

import java.util.UUID;

public class UUIDUtil {

    public static String getuuid()
    {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
