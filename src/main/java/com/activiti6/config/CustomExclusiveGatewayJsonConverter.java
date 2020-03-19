package com.activiti6.config;

import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.editor.language.json.converter.ExclusiveGatewayJsonConverter;
import org.springframework.util.StringUtils;

import com.activiti6.util.BpmnUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CustomExclusiveGatewayJsonConverter extends ExclusiveGatewayJsonConverter{

	@Override
	protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
		// TODO Auto-generated method stub
		super.convertElementToJson(propertiesNode, baseElement);
	}

	@Override
	protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode,
			Map<String, JsonNode> shapeMap) {
		// TODO Auto-generated method stub
		FlowElement flowElement= super.convertJsonToElement(elementNode, modelNode, shapeMap);
	    ObjectNode properties  = (ObjectNode) elementNode.get("properties");
	    String chooseClass=properties.get(ExtAttrKeys.exclGateWay_chooseClass).textValue();
	    if(StringUtils.hasLength(chooseClass )){
	    	BpmnUtils.addElementAttr(flowElement, ExtAttrKeys.exclGateWay_chooseClass,chooseClass );
	    }
	    return flowElement;
	}
	
}
