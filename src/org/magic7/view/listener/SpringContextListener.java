package org.magic7.view.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringContextListener implements ServletContextListener 
{
	private static WebApplicationContext springContext;

	public SpringContextListener() 
	{
		super();
	}

	public void contextInitialized(ServletContextEvent event) 
	{
		springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
	}


	public void contextDestroyed(ServletContextEvent event)
	{
	
	}

	public static ApplicationContext getApplicationContext() 
	{
		return springContext;
	}


}
