package com.wink.livemall.admin.util;

import java.security.MessageDigest;

/**
 * 密码md5加密处理
 */
public class Md5Util {

	public static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16)
					sb.append("0");
				sb.append(Integer.toHexString(val));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
	    System.out.println("----------------------");
	    System.out.println(MD5("15762234"+"78397036f19f479da4a1cc29d18fb5a4"+System.currentTimeMillis()));
	}
	
	public static String md5jiami(String s){
	    return org.apache.commons.codec.digest.DigestUtils.md5Hex(s);
	}
}
