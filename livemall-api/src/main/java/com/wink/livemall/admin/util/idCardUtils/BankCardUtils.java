package com.wink.livemall.admin.util.idCardUtils;

import com.wink.livemall.admin.api.user.BankController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BankCardUtils {
    private static Logger logger =  LogManager.getLogger(BankController.class);

    /**
     * 信用卡luhm规则检核
     * @param bankCardNo
     * @return
     */
    public static boolean luhmCheck(String bankCardNo){
        int s1 = 0;
        int s2 = 0;
        String reverse = new StringBuffer(bankCardNo).reverse().toString();

        for(int i=0; i<reverse.length(); i++){
            int digit = Character.digit(reverse.charAt(i), 10);

            if (digit < 0) {//存在非数字字符
                logger.info("卡号中存在非法字符: "+reverse.charAt(i));
                return false;
            }

            if(i % 2 == 0){//this is for odd digits, they are 1-indexed in the algorithm
                s1 += digit;
            }else{//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digit;
                if(digit >= 5){
                    s2 -= 9;
                }
            }
        }
        return (s1 + s2) % 10 == 0;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * 该校验的过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")||nonCheckCodeCardId.trim().length()<15
                ||nonCheckCodeCardId.trim().length()>18) {
            //如果传的数据不合法返回N
            System.out.println("银行卡号不合法！");
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        // 执行luh算法
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {  //偶数位处理
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if(bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }
    public static void main(String[] args) {
//        System.out.println(checkBankCard("6212261001088612921"));
    }
}
