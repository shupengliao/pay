package com.shupeng.liao.wechat.pay.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.shupeng.liao.wechat.pay.model.UnifiedOrderResponseData;

/**
 * @ClassName: Signature
 * @Description:微信返回数据的校验
 * @author: shupeng.liao
 * @date: 2017年4月12日 下午1:41:42
 * @version: 1.0.0
 */
public class Signature {
    // 签名
    public static String getSign(Object o, String key) {
	List<Object> list = new ArrayList<Object>();
	Class<?> cls = o.getClass();
	Field[] fields = cls.getDeclaredFields();
	for (Field f : fields) {
	    f.setAccessible(true);
	    try {
		if ((f.get(o) != null) && (f.get(o) != "")) {
		    list.add(f.getName() + "=" + f.get(o) + "&");
		}
	    } catch (IllegalArgumentException e) {
		LogUtil.error(Signature.class, "微信签名失败 IllegalArgumentException");
	    } catch (IllegalAccessException e) {
		LogUtil.error(Signature.class, "微信签名失败 IllegalArgumentException");
	    }
	}
	int size = list.size();
	String[] arrayToSort = (String[]) list.toArray(new String[size]);
	Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < size; ++i) {
	    sb.append(arrayToSort[i]);
	}
	String result = sb.toString();
	result = result + "key=" + key;
	LogUtil.debug("Sign Before MD5:" + result);
	result = MD5.getCode(result).toUpperCase();
	LogUtil.debug("Sign Result:" + result);
	return result;
    }

    public static String getSign(Map<String, Object> map, String key) {
	ArrayList<Object> list = new ArrayList<Object>();
	for (Entry<String, Object> entry : map.entrySet()) {
	    if (entry.getValue() != "") {
		list.add(((String) entry.getKey()) + "=" + entry.getValue() + "&");
	    }
	}
	int size = list.size();
	String[] arrayToSort = (String[]) list.toArray(new String[size]);
	Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < size; ++i) {
	    sb.append(arrayToSort[i]);
	}
	String result = sb.toString();
	result = result + "key=" + key;

	result = MD5.getCode(result).toUpperCase();

	return result;
    }

    public static String getSignFromResponseString(String responseString, String key)
	    throws IOException, SAXException, ParserConfigurationException {
	Map<String, Object> map = XmlUtil.getMapFromXML(responseString);

	map.put("sign", "");

	return getSign(map, key);
    }

    public static boolean checkIsSignValidFromResponseString(String responseString, String key)
	    throws ParserConfigurationException, IOException, SAXException {
	Map<String, Object> map = XmlUtil.getMapFromXML(responseString);
	LogUtil.debug(map.toString());
	String signFromAPIResponse = map.get("sign").toString();
	if ((signFromAPIResponse == "") || (signFromAPIResponse == null)) {
	    LogUtil.error(Signature.class, "API返回的数据签名数据不存在，有可能被第三方篡改!!!");
	    return false;
	}
	LogUtil.debug("服务器回包里面的签名是:" + signFromAPIResponse);

	map.put("sign", "");

	String signForAPIResponse = getSign(map, key);

	if (!(signForAPIResponse.equals(signFromAPIResponse))) {
	    LogUtil.error(Signature.class, "API返回的数据签名验证不通过，有可能被第三方篡改!!!");
	    return false;
	}
	LogUtil.debug("恭喜，API返回的数据签名验证通过!!!");
	return true;
    }

    /**
     * @param xml
     *            响应的xml
     * @Description: 获取预支付id(prepay_id)
     * @return String
     */
    public static String getPrepayId(String xml, String key) {

	// xml为空时
	if (xml == null || "".equals(xml)) {
	    LogUtil.error(Signature.class, "响应数据xml为空");
	}

	// 获得响应的数据
	UnifiedOrderResponseData resData = XmlUtil.ParserStringToObject(UnifiedOrderResponseData.class, xml);
	// 没数据时
	if (resData == null) {
	    LogUtil.error(Signature.class, "微信获取prepayIdxml解析时解析数据为空");
	}
	// 失败
	if ("FAIL".equals(resData.getReturn_code())) {
	    LogUtil.error(Signature.class, "微信获取prepayIdxml时return_code为fail;错误信息:" + resData.getReturn_msg());
	}
	// 业务结果FAIL
	if ("FAIL".equals(resData.getResult_code())) {
	    LogUtil.error(Signature.class, "微信获取prepayIdxml时result_code为fail;错误代码:" + resData.getErr_code() + ";错误代码描述"
		    + resData.getErr_code_des());
	}
	// 获得prepay_id
	if (resData.getPrepay_id() == null || "".equals(resData.getPrepay_id())) {
	    LogUtil.error(Signature.class, "微信获取prepayIdxml时prepay_id为空");
	}
	boolean b = false;
	// 校验数据
	try {
	    b = checkIsSignValidFromResponseString(xml, key);
	} catch (ParserConfigurationException e) {
	    LogUtil.error(Signature.class, "微信返回数据签名解析错误");
	} catch (IOException e) {
	    LogUtil.error(Signature.class, "微信返回数据签名IOException");
	} catch (SAXException e) {
	    LogUtil.error(Signature.class, "微信返回数据签名SAXException");
	}
	if (b) {
	    return resData.getPrepay_id();
	} else {
	    LogUtil.error(Signature.class, "微信返回数据签名未通过");
	}
	return xml;
    }
}