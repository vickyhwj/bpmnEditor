package com.activiti6.util;

import java.util.HashMap;
import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.CustomProperty;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.UserTask;

public class BpmnUtils {
	public static void addTaskCustomProperties(UserTask userTask,String key,String value){
		CustomProperty customProperty=new CustomProperty();
	    customProperty.setName(key);
	    customProperty.setSimpleValue(value);
	    userTask.getCustomProperties().add(customProperty);
	}
	
	public static void addElementAttr(BaseElement baseElement,String key,String value){
		ExtensionAttribute extensionAttribute=new ExtensionAttribute(key);
		extensionAttribute.setValue(value);
		baseElement.addAttribute(extensionAttribute);;
	}

	public static Map<String,String> getExtensionMap(BaseElement element){
		Map<String,String> map=new HashMap<>();
		for(String key:  element.getExtensionElements().keySet()){
			map.put(key, element.getExtensionElements().get(key).get(0).getElementText());
		}
		return map;
	}
}
