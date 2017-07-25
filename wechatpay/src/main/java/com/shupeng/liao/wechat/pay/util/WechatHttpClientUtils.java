package com.shupeng.liao.wechat.pay.util;

import java.net.SocketTimeoutException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.ConnectionPoolTimeoutException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @ClassName: HttpClientUtils
 * @Description:主要是请求时附带xml其他同样请求也可使用
 * @author: shupeng.liao
 * @date: 2017年4月12日 上午11:28:58
 * @version: 1.0.0
 */
public class WechatHttpClientUtils {

    private boolean hasInit = false;

    private int socketTimeout = 10000;// 指的是请求超时

    private int connectTimeout = 30000;// 指的是响应超时

    private RequestConfig requestConfig;

    private CloseableHttpClient httpClient;

    private void init() {
	this.requestConfig = RequestConfig.custom().setSocketTimeout(this.socketTimeout)
		.setConnectTimeout(this.connectTimeout).build();
	this.hasInit = true;
    }

    /**
     * @param XML
     *            上传xml
     * @Description: 返回请求的数据xml
     * @return String xml
     */
    public String sendPost(String url, String xml) {
	if (!(this.hasInit)) {
	    init();
	}
	String result = null;

	HttpPost httpPost = new HttpPost(url);

	LogUtil.debug(this.getClass(), "API，POST过去的数据是：" + xml);

	StringEntity postEntity = new StringEntity(xml, "UTF-8");
	// 请求头
	httpPost.addHeader("Content-Type", "text/xml");
	// 添加上传的字符串
	httpPost.setEntity(postEntity);
	// 添加配置
	httpPost.setConfig(this.requestConfig);

	LogUtil.debug("executing request" + httpPost.getRequestLine());

	try {
	    // 初始化
	    httpClient = HttpClients.custom().build();
	    // 执行
	    HttpResponse response = this.httpClient.execute(httpPost);
	    // 响应数据
	    HttpEntity entity = response.getEntity();
	    result = EntityUtils.toString(entity, "UTF-8");
	} catch (ConnectionPoolTimeoutException e) {
	    LogUtil.error(this.getClass(), "http get throw ConnectionPoolTimeoutException(wait time out)");
	} catch (ConnectTimeoutException e) {
	    LogUtil.error(this.getClass(), "http get throw ConnectTimeoutException");
	} catch (SocketTimeoutException e) {
	    LogUtil.error(this.getClass(), "http get throw SocketTimeoutException");
	} catch (Exception e) {
	    LogUtil.error(this.getClass(), "http get throw Exception");
	} finally {
	    httpPost.abort();
	}

	return result;
    }

    public void setSocketTimeout(int socketTimeout) {
	this.socketTimeout = socketTimeout;
	resetRequestConfig();
    }

    public void setConnectTimeout(int connectTimeout) {
	this.connectTimeout = connectTimeout;
	resetRequestConfig();
    }

    private void resetRequestConfig() {
	this.requestConfig = RequestConfig.custom().setSocketTimeout(this.socketTimeout)
		.setConnectTimeout(this.connectTimeout).build();
    }

    public void setRequestConfig(RequestConfig requestConfig) {
	this.requestConfig = requestConfig;
    }
}
