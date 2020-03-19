package com.activiti6.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.CustomProperty;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.BaseBpmnJsonConverter;
import org.activiti.editor.language.json.converter.SequenceFlowJsonConverter;
import org.activiti.editor.language.json.converter.UserTaskJsonConverter;
import org.springframework.util.StringUtils;

import com.activiti6.config.handler.ExtenisonHandler;
import com.activiti6.config.handler.JSONHandler;
import com.activiti6.entity.NextNode;
import com.activiti6.util.BpmnUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
	
public class SeqFlowJsonConverter extends SequenceFlowJsonConverter {
	
    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
        super.convertElementToJson(propertiesNode, baseElement);
      
        
        //解析新增属性的业务逻辑
    }
    
    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
        FlowElement flowElement = super.convertJsonToElement(elementNode,
                modelNode, shapeMap);
        //解析新增属性的业务逻辑
        ObjectNode properties  = (ObjectNode) elementNode.get("properties");
        
        SequenceFlow sf= (SequenceFlow) flowElement;
        if(!StringUtils.hasLength( sf.getConditionExpression())){
        	JsonNode sourceNode=getJsonNodeByRef(shapeMap, sf.getSourceRef());
        	JsonNode targetNode=getJsonNodeByRef(shapeMap, sf.getTargetRef());
        	
        	ArrayNode outgoings= (ArrayNode) sourceNode.get("outgoing");
        	if(outgoings.size()<=1){
        		
        	}
        	else{
	        	String stencilSource=((ObjectNode)sourceNode.get("stencil")).get("id").asText();
	        	String stencilTarget=((ObjectNode)targetNode.get("stencil")).get("id").asText();
	        	if(STENCIL_GATEWAY_PARALLEL.equals(stencilSource)){
	        	
	        	}else{
		        	String seqid= ((ObjectNode)elementNode.get("properties")).get("overrideid").asText();
		        	if(!StringUtils.hasLength(seqid)){
		        		seqid=elementNode.get("resourceId").asText();
		        	}
		        	sf.setConditionExpression("${"+ExtAttrKeys.nextNode+"=='"+seqid+"'}");
	        	}
        	}
        }
        
        if(StringUtils.hasLength( properties.get(ExtAttrKeys.seq_order).textValue())){
        	BpmnUtils.addElementAttr(flowElement, ExtAttrKeys.seq_order, properties.get(ExtAttrKeys.seq_order).textValue());
        }
     
        return sf;
    }
    
    private JsonNode getJsonNodeByRef( Map<String, JsonNode> shapeMap ,String ref){
    	JsonNode jsonNode= shapeMap.get( ref);
    	if(jsonNode==null){
    		for(Entry<String, JsonNode> e: shapeMap.entrySet()){
    			String overrideid=e.getValue().get("properties").get("overrideid").asText();
    			if(ref.equals(overrideid)){
    				jsonNode= e.getValue();
    				break;
    			}
    		}
    	}
    	return jsonNode;
    }
    
    public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap,
            Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    
        fillJsonTypes(convertersToBpmnMap);
        fillBpmnTypes(convertersToJsonMap);
    }
  
    public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
        convertersToBpmnMap.put(STENCIL_SEQUENCE_FLOW, SeqFlowJsonConverter.class);
    }
  
    public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
        convertersToJsonMap.put(SequenceFlow.class, SeqFlowJsonConverter.class);
    }
    
}