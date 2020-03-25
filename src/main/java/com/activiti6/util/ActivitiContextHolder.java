package com.activiti6.util;

public class ActivitiContextHolder {
	public static ThreadLocal<ActivitiContext> activitiContext=new ThreadLocal<>();
	
	public static void initContext(){
		activitiContext.set(new ActivitiContext());
	}
	
	public static void clearContext(){
		activitiContext.remove();
	}
	public static ActivitiContext getContext(){
		return activitiContext.get();
	}
}
