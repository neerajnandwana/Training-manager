package com.mgr.training.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Throwables;

public class Prop {
	private static final Log LOG = LogFactory.getLog(Prop.class);
	public final static PropertiesConfiguration applicationConfig = loadConfig(Const.APPLICATION_PROPERTIES);
	public final static PropertiesConfiguration persistenceConfig = loadConfig(Const.PERSISTENCE_PROPERTIES);

	private Prop() {
	}

	private static PropertiesConfiguration loadConfig(final String fileName) {
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration(fileName);
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			LOG.debug("property file loaded : " + config.getBasePath());
		} catch (ConfigurationException e) {
			LOG.error("Error while loading the config file in startup: " + fileName, e);
			throw Throwables.propagate(e);
		}
		return config;
	}

	public static boolean isDevMode() {
		String mode = applicationConfig.getString(Const.APPLICATION_ENV_MODE, "prod");
		return "dev".equalsIgnoreCase(mode);
	}
}
