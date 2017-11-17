package org.magic7.view.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.magic7.dynamic.loader.MagicLoaderUtils;

public class RegionClassGenerateListener implements ServletContextListener {

	public RegionClassGenerateListener() {
		super();
	}

	public void contextInitialized(ServletContextEvent event) {
		MagicLoaderUtils.generateAllRegionClass();
	}

	public void contextDestroyed(ServletContextEvent event) {

	}

}
