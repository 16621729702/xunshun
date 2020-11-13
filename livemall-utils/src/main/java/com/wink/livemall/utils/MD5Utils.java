package com.wink.livemall.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {

	private static MessageDigest mdigest = null;
	private static char digits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	private static MessageDigest getMdInst() {
		if (null == mdigest) {
			try {
				mdigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return mdigest;
	}

	public static String encode(String s) {
		if(null == s) {
			return "";
		}

		try {
			byte[] bytes = s.getBytes();
			getMdInst().update(bytes);
			byte[] md = getMdInst().digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = digits[byte0 >>> 4 & 0xf];
				str[k++] = digits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String encrypt(String message) throws Exception {
		return encrypt(message, "MD5");
	}

	// algorithm: MD5 or SHA-1
	// return string length: 32 if algorithm = MD5, or 40 if algorithm = SHA-1
	public static String encrypt(String message, String algorithm)
			throws Exception {
		if (message == null) {
			throw new Exception("message is null.");
		}
		if (!"MD5".equals(algorithm) && !"SHA-1".equals(algorithm)) {
			throw new Exception("algorithm must be MD5 or SHA-1.");
		}
		byte[] buffer = message.getBytes();

		// The SHA algorithm results in a 20-byte digest, while MD5 is 16 bytes
		// long.
		MessageDigest md = MessageDigest.getInstance(algorithm);

		// Ensure the digest's buffer is empty. This isn't necessary the first
		// time used.
		// However, it is good practice to always empty the buffer out in case
		// you later reuse it.
		md.reset();

		// Fill the digest's buffer with data to compute a message digest from.
		md.update(buffer);

		// Generate the digest. This does any necessary padding required by the
		// algorithm.
		byte[] digest = md.digest();

		// Save or print digest bytes. Integer.toHexString() doesn't print
		// leading zeros.
		StringBuffer hexString = new StringBuffer();
		String sHexBit = null;
		for (int i = 0; i < digest.length; i++) {
			sHexBit = Integer.toHexString(0xFF & digest[i]);
			if (sHexBit.length() == 1) {
				sHexBit = "0" + sHexBit;
			}
			hexString.append(sHexBit);
		}
		return hexString.toString();
	}

	public static String getMD5(String str) {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			String md5=new BigInteger(1, md.digest()).toString(16);
			//BigInteger会把0省略掉，需补全至32位
			return fillMD5(md5);
		} catch (Exception e) {
			throw new RuntimeException("MD5加密错误:"+e.getMessage(),e);
		}
	}

	private static String fillMD5(String md5){
		return md5.length()==32?md5:fillMD5("0"+md5);
	}


	public static String getMD5(byte[] source) {
		String s = null;
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
		try {
			MessageDigest md = MessageDigest
					.getInstance("MD5");
			md.update(source);
            byte[] tmp = md.digest();

            char[] str = new char[16 * 2];

			int k = 0;
			for (int i = 0; i < 16; i++) {

				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void main(String[] xu) {
		try {
			System.out.println(getMD5("a".getBytes()));
			System.out.println(encrypt("Train2222"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
