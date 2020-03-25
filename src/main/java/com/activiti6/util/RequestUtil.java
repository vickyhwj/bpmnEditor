package com.activiti6.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
	public static Map<String,Object> getafTParam(HttpServletRequest request){
		Map<String,Object> pMap=new HashMap<String, Object>(); 
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if(paramName.startsWith("afT_")){
				pMap.put(paramName, request.getParameter(paramName));
			}
		}
		return pMap;
	}
	
	public static Map<String,Object> getafPParam(HttpServletRequest request){
		Map<String,Object> pMap=new HashMap<String, Object>(); 
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if(paramName.startsWith("afP_")){
				pMap.put(paramName, request.getParameter(paramName));
			}
		}
		return pMap;
	}

	public static Map<String, Object> getafLParam(HttpServletRequest request) {
		Map<String,Object> pMap=new HashMap<String, Object>(); 
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if(paramName.startsWith("afL_")){
				pMap.put(paramName, request.getParameter(paramName));
			}
		}
		return pMap;
	}

}
