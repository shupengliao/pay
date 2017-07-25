package com.shupeng.liao.wechat.pay.model;

import java.io.Serializable;

/**
 * @ClassName: Configure
 * @Description:微信的一般常量
 * @author: shupeng.liao
 * @date: 2017年4月12日 下午2:27:46
 * @version: 1.0.0
 */
public class Configure implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2854383832775948948L;

    private String key;

    private String appID;

    private String appsecret;

    private String mchID;

    private String notify_url;

    public static String PAY_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public Configure() {
    }

    public Configure(String key, String appID, String appsecret, String mchID, String notify_url) {
	this.appID = appID;
	this.key = key;
	this.appsecret = appsecret;
	this.mchID = mchID;
	this.notify_url = notify_url;
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getAppID() {
	return appID;
    }

    public void setAppID(String appID) {
	this.appID = appID;
    }

    public String getAppsecret() {
	return appsecret;
    }

    public void setAppsecret(String appsecret) {
	this.appsecret = appsecret;
    }

    public String getMchID() {
	return mchID;
    }

    public void setMchID(String mchID) {
	this.mchID = mchID;
    }

    public String getNotify_url() {
	return notify_url;
    }

    public void setNotify_url(String notify_url) {
	this.notify_url = notify_url;
    }

}
