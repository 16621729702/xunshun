package com.wink.livemall.admin.util;

import java.math.BigDecimal;

/**
 * 废弃
 * 成长值算法
 */
public class Growth_valueUtil {

    /**
     * 成长值算法
     */
    public Integer getgrowth_value(Integer localvalue,Integer momey,String type){
        Integer newgrowth_value = localvalue;
        //订单完成
        if("add".equals(type)){
            if(momey!=null&&momey>=10){
                Integer addvalue = momey/12;
                if(addvalue==0){
                    newgrowth_value +=1;
                }else {
                    newgrowth_value+=addvalue;
                }
            }
        }
        //减少成长值
        if("delete".equals(type)){
            if(momey!=null){
                Integer deletevalue = (momey/12)*2;
                if(deletevalue==0){
                    newgrowth_value -=1;
                }else {
                    newgrowth_value-=deletevalue;
                }
            }
        }
        return newgrowth_value;
    }
}
