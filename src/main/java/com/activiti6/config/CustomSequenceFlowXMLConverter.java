package com.activiti6.config;

import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.activiti.bpmn.converter.SequenceFlowXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.SequenceFlow;

public class CustomSequenceFlowXMLConverter extends SequenceFlowXMLConverter{
	

	@Override
	protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
		// TODO Auto-generated method stub
		
		BaseElement baseElement= super.convertXMLToElement(xtr, model);
//		for(int i=0;i< xtr.getAttributeCount();++i){
//			System.out.println(xtr.getAttributeValue(i));
//		}
//		BpmnXMLUtil.parseExtensionElement(xtr);
		return baseElement;
	}

	@Override
	public void convertToXML(XMLStreamWriter xtw, BaseElement baseElement, BpmnModel model) throws Exception {
		// TODO Auto-generated method stub
		super.convertToXML(xtw, baseElement, model);
		
//		BpmnXMLUtil.writeCustomAttributes(Arrays.asList(baseElement.getAttributes().get("mroe")), xtw,null);
	}

	@Override
	protected void writeAdditionalAttributes(BaseElement element, BpmnModel model, XMLStreamWriter xtw)
			throws Exception {
		// TODO Auto-generated method stub
		super.writeAdditionalAttributes(element, model, xtw);
		
//	    writeDefaultAttribute("mroe","fuck", xtw);
	    
		
	}
	
	 @Override
	  protected boolean writeExtensionChildElements(BaseElement element, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
		 super.writeExtensionChildElements(element, didWriteExtensionStartElement, xtw);
	    SequenceFlow sequenceFlow = (SequenceFlow) element;
	    
	    for(List<ExtensionAttribute> attributes:  sequenceFlow.getAttributes().values()){
	  
	      for (ExtensionAttribute attribute : attributes) {
	        
	      
	        
	        if (didWriteExtensionStartElement == false) {
	          xtw.writeStartElement(ELEMENT_EXTENSIONS);
	          didWriteExtensionStartElement = true;
	        }
	        xtw.writeStartElement(ACTIVITI_EXTENSIONS_PREFIX, attribute.getName(), ACTIVITI_EXTENSIONS_NAMESPACE);
	        xtw.writeCharacters(attribute.getValue());
	        xtw.writeEndElement();
	      }
	    }
	    return didWriteExtensionStartElement;
	  }

}
