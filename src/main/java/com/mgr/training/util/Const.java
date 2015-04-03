package com.mgr.training.util;

import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.TimeZone;

import org.apache.log4j.helpers.ISO8601DateFormat;

import com.google.common.base.Charsets;
import com.sun.management.OperatingSystemMXBean;

public class Const {
	public static final String userId = "userId";
	public static final String userPass = "password";
	public static final String PERSISTENCE_UNIT_NAME = "trainingMgrJpaUnit";

	public static final String THREAD_POOL_SIZE_KEY = "app.threadpool.size";
	public static final String HASH_REPEAT_KEY = "app.security.hash.repeat";
	public static final String HASH_SALT_KEY = "app.security.hash.salt";
	public static final String DISKSPACE_THRESHOLD_KEY = "app.health.diskspace.threshold";
	public static final String DB_QUERY_KEY = "app.health.db.query";
	

	public static final OperatingSystemMXBean OS_BEAN = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	public static final DateFormat ISO_DATETIME_FORMAT = new ISO8601DateFormat();
	public static final Charset CHAR_SET = Charsets.UTF_8;

	public static final String APPLICATION_PROPERTIES = "application.properties";
	public static final String PERSISTENCE_PROPERTIES = "persistence.properties";
	public static final String APPLICATION_ENV_MODE = "app.env.mode";

	static {
		ISO_DATETIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private Const() {
	}
}
