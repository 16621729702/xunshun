package com.wink.livemall.job.utils;

import com.tencentyun.TLSSigAPIv2;

public class IMUtil {

    public static int sakappid = 1400404340;
    public static String key="d99a57a24b9dd20a47f628b2cfbaba0569041feeec88fd651e87aa3641ec6288";
    public static String userid = "administrator";

    static TLSSigAPIv2 tlsSigAPIv2 = new TLSSigAPIv2(sakappid,key);

    public static String genSig(){
        return tlsSigAPIv2.genSig(IMUtil.userid, 180*86400);
    }
    public static String genSig(String userid){
        return tlsSigAPIv2.genSig(userid, 180*86400);
    }

    public static void main(String[] args) {
        System.out.println(IMUtil.genSig());
    }
}
