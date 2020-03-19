package com.activiti6.util;

import org.springframework.context.ConfigurableApplicationContext;

public class BeanUtil {
    //将管理上下文的applicationContext设置成静态变量，供全局调用
    public static ConfigurableApplicationContext applicationContext;
    //定义一个获取已经实例化bean的方法

	public static ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		BeanUtil.applicationContext = applicationContext;
	}
    
}
