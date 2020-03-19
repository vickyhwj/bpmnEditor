package com.activiti6.config.handler;

import org.activiti.bpmn.model.CustomProperty;
import org.activiti.bpmn.model.UserTask;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import net.sf.json.JSONObject;

public class ExtHandler implements JSONHandler{

	@Override
	public String getFieldName() {
		// TODO Auto-generated method stub
		return "ext";
	}

	@Override
	public void handle(JsonNode node, UserTask userTask) {
		// TODO Auto-generated method stub
		   	String extValue=node.asText(); 
//		   	if(StringUtils.hasLength(extValue)){
//				JSONObject jsonObject=new JSONObject().fromObject(extValue);
//				System.out.println(jsonObject);
//		   	}
			
	        CustomProperty customProperty=new CustomProperty();
	        customProperty.setName(getFieldName());
	        customProperty.setSimpleValue(node.toString());
	        userTask.getCustomProperties().add(customProperty);
	}

}
