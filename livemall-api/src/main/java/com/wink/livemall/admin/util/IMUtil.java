package com.wink.livemall.admin.util;

import com.tencentyun.TLSSigAPIv2;

/**
 * 腾讯im获取sign 工具类
 */
public class IMUtil {



    public static String genSig(long sakappid,String key,String userid){
        TLSSigAPIv2 tlsSigAPIv2 = new TLSSigAPIv2(sakappid,key);
        return tlsSigAPIv2.genSig(userid, 180*86400);
    }


}
