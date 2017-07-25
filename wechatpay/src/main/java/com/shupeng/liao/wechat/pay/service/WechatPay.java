package com.shupeng.liao.wechat.pay.service;

import com.shupeng.liao.wechat.pay.model.Configure;
import com.shupeng.liao.wechat.pay.model.UnifiedOrder;
import com.shupeng.liao.wechat.pay.util.Signature;
import com.shupeng.liao.wechat.pay.util.WechatHttpClientUtils;
import com.shupeng.liao.wechat.pay.util.XmlUtil;

public class WechatPay {

    // 获得预支付id
    public String getPrepayId(UnifiedOrder order, Configure configure) {
	// 初始化工具类
	WechatHttpClientUtils httpClient = new WechatHttpClientUtils();
	// 实体转换xml
	String xml = XmlUtil.ObjectToXml(order);
	// 获得响应数据
	String resdata = httpClient.sendPost(Configure.PAY_API, xml);
	// 获得预支付id
	String prepayId = Signature.getPrepayId(resdata, configure.getKey());
	return prepayId;
    }
}
