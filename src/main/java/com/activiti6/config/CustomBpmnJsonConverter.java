package com.activiti6.config;

import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;

public class CustomBpmnJsonConverter extends BpmnJsonConverter {
    
    //通过继承开放convertersToJsonMap的访问
    public static Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> getConvertersToJsonMap(){
        return convertersToJsonMap;
    }
 
    //通过继承开放convertersToJsonMap的访问
    public static Map<String, Class<? extends BaseBpmnJsonConverter>> getConvertersToBpmnMap(){
        return convertersToBpmnMap;
    }
}