package com.activiti6.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.CustomProperty;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.UserTaskJsonConverter;
import org.springframework.util.StringUtils;

import com.activiti6.config.handler.ExtHandler;
import com.activiti6.config.handler.ExtenisonHandler;
import com.activiti6.config.handler.JSONHandler;
import com.activiti6.util.BpmnUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
	
public class CustomUserTaskJsonConverter extends UserTaskJsonConverter {
	
    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
        super.convertElementToJson(propertiesNode, baseElement);
        UserTask userTask = (UserTask) baseElement;
        
        //解析新增属性的业务逻辑
    }
    
    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
        FlowElement flowElement = super.convertJsonToElement(elementNode,
                modelNode, shapeMap);
        //解析新增属性的业务逻辑
        ObjectNode properties  = (ObjectNode) elementNode.get("properties");
        
        String ext=properties.get(ExtAttrKeys.userTask_ext).textValue();
        String taskChooseClass=properties.get(ExtAttrKeys.userTask_chooseClass).textValue();
        if(StringUtils.hasLength(ext)){
        	BpmnUtils.addTaskCustomProperties((UserTask) flowElement,ExtAttrKeys.userTask_ext, ext);
        }
        if(StringUtils.hasLength(taskChooseClass)){
        	BpmnUtils.addTaskCustomProperties((UserTask) flowElement, ExtAttrKeys.userTask_chooseClass, taskChooseClass);
        }
        
        return flowElement;
    }
    
    public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap,
            Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    
        fillJsonTypes(convertersToBpmnMap);
        fillBpmnTypes(convertersToJsonMap);
    }
  
    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
        convertersToBpmnMap.put(STENCIL_TASK_USER, CustomUserTaskJsonConverter.class);
    }
  
    public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
        convertersToJsonMap.put(UserTask.class, CustomUserTaskJsonConverter.class);
    }
    
}