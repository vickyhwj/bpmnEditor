package com.activiti6.config.handler;

import org.activiti.bpmn.model.CustomProperty;
import org.activiti.bpmn.model.UserTask;

import com.fasterxml.jackson.databind.JsonNode;

public class ExtenisonHandler implements JSONHandler{

	@Override
	public String getFieldName() {
		// TODO Auto-generated method stub
		return "extenison";
	}

	@Override
	public void handle(JsonNode node, UserTask userTask) {
		// TODO Auto-generated method stub
		   	String extValue=node.asText(); 
	   
	        CustomProperty customProperty=new CustomProperty();
	        customProperty.setName(getFieldName());
	        customProperty.setSimpleValue(node.toString());
	        userTask.getCustomProperties().add(customProperty);
	}

}
