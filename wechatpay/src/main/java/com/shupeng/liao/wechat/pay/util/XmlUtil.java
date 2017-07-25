package com.shupeng.liao.wechat.pay.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * @ClassName: XmlUtil
 * @Description:xml的公用类
 * @author: shupeng.liao
 * @date: 2017年4月12日 上午11:00:12
 * @version: 1.0.0
 */
public class XmlUtil {
    /**
     * @param t
     *            类
     * @param xml
     *            xml字符串
     * @return
     * @Description:解析xml生成对应实例
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public static <T> T ParserStringToObject(Class<T> t, String xml) {
	XStream xStreamForResponseData = new XStream();
	xStreamForResponseData.alias("xml", t);
	// 忽略不知道的元素
	xStreamForResponseData.ignoreUnknownElements();
	return (T) xStreamForResponseData.fromXML(xml);
    }

    /**
     * @param t
     *            类
     * @param XMLobject
     *            实例
     * @Description: 将对应实体类转换成xml
     * @return String
     */
    public static String ObjectToXml(Object XMLobject) {
	XStream xStreamForRequestPostData = new XStream(new XppDriver(new NoNameCoder()) {

	    @Override
	    public HierarchicalStreamWriter createWriter(Writer out) {
		return new PrettyPrintWriter(out) {
		    // 对所有xml节点的转换都增加CDATA标记
		    boolean cdata = true;

		    @Override
		    @SuppressWarnings("rawtypes")
		    public void startNode(String name, Class clazz) {
			super.startNode(name, clazz);
		    }

		    @Override
		    public String encodeNode(String name) {
			return name;
		    }

		    @Override
		    protected void writeText(QuickWriter writer, String text) {
			if (cdata) {
			    writer.write("<![CDATA[");
			    writer.write(text);
			    writer.write("]]>");
			} else {
			    writer.write(text);
			}
		    }
		};
	    }
	});
	// 使用注解的类
	xStreamForRequestPostData.processAnnotations(XMLobject.getClass());
	// 扫描
	xStreamForRequestPostData.autodetectAnnotations(true);
	// 解析
	String XML = xStreamForRequestPostData.toXML(XMLobject);
	return XML;
    }

    public static Map<String, Object> getMapFromXML(String xmlString)
	    throws ParserConfigurationException, IOException, SAXException {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	InputStream is = getStringStream(xmlString);
	Document document = builder.parse(is);

	NodeList allNodes = document.getFirstChild().getChildNodes();

	Map<String, Object> map = new HashMap<String, Object>();
	int i = 0;
	while (i < allNodes.getLength()) {
	    Node node = allNodes.item(i);
	    if (node instanceof Element) {
		map.put(node.getNodeName(), node.getTextContent());
	    }
	    ++i;
	}
	return map;
    }

    public static InputStream getStringStream(String sInputString) {
	ByteArrayInputStream tInputStringStream = null;
	if ((sInputString != null) && (!(sInputString.trim().equals("")))) {
	    tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
	}
	return tInputStringStream;
    }
}
