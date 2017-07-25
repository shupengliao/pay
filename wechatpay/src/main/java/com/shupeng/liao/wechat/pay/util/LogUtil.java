package com.shupeng.liao.wechat.pay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void init(Class<?> c) {
	logger = LoggerFactory.getLogger(c);
    }

    public static String error(Class<?> c, Object log) {
	init(c);
	logger.error(log.toString());
	return log.toString();
    }

    public static String debug(Class<?> c, Object log) {
	init(c);
	logger.debug(log.toString());
	return log.toString();
    }

    public static String info(Class<?> c, Object log) {
	init(c);
	logger.info(log.toString());
	return log.toString();
    }

    public static String warn(Class<?> c, Object log) {
	init(c);
	logger.warn(log.toString());
	return log.toString();
    }

    public static String trace(Class<?> c, Object log) {
	init(c);
	logger.trace(log.toString());
	return log.toString();
    }

    public static String error(Object log) {
	logger.error(log.toString());
	return log.toString();
    }

    public static String debug(Object log) {
	logger.debug(log.toString());
	return log.toString();
    }

    public static String info(Object log) {
	logger.info(log.toString());
	return log.toString();
    }

    public static String warn(Object log) {
	logger.warn(log.toString());
	return log.toString();
    }

    public static String trace(Object log) {
	logger.trace(log.toString());
	return log.toString();
    }
}
