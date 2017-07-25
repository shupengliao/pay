package com.shupeng.liao.wechat.pay.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    // 得到加密结果
    public static String getCode(String str) {
	/*
	 * String resultString = null; try { resultString = new String(strObj);
	 * MessageDigest md = MessageDigest.getInstance("MD5"); // md.digest()
	 * 该函数返回值为存放哈希值结果的byte数组 resultString =
	 * byteToString(md.digest(strObj.getBytes())); } catch
	 * (NoSuchAlgorithmException ex) { ex.printStackTrace(); } return
	 * resultString;
	 */

	if (str != null && !str.equals("")) {
	    try {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < md5Byte.length; i++) {
		    sb.append(HEX[(int) (md5Byte[i] & 0xff) / 16]);
		    sb.append(HEX[(int) (md5Byte[i] & 0xff) % 16]);
		}
		str = sb.toString();
	    } catch (NoSuchAlgorithmException e) {
	    } catch (Exception e) {
	    }
	}
	return str;
    }

}
