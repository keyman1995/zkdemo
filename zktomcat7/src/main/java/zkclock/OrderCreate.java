package zkclock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderCreate {



    public String getOrder(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(date);
    }

}
