package com.activiti6.config.handler;

import org.activiti.bpmn.model.CustomProperty;
import org.activiti.bpmn.model.UserTask;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class BaseExtenisonHandler implements JSONHandler{

	
	public void addProperties(UserTask userTask,String key,String value){
		 CustomProperty customProperty=new CustomProperty();
	     customProperty.setName(key);
	     customProperty.setSimpleValue(value);
	     userTask.getCustomProperties().add(customProperty);
	}
	
	
	

}
