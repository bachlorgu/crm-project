package crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//日期格式处理类
public class DateUtil {

    public static String timeFormat1(Date date)
    {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s=sdf.format(date);
        return s;

    }

    public static String timeFormat2(Date date)
    {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String s=sdf.format(date);
        return s;
    }

    public static String timeFormat3(Date date)
    {

        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        String s=sdf.format(date);
        return s;
    }

}
