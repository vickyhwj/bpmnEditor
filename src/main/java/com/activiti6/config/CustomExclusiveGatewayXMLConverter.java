package com.activiti6.config;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import org.activiti.bpmn.converter.ExclusiveGatewayXMLConverter;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.ExtensionAttribute;

public class CustomExclusiveGatewayXMLConverter extends ExclusiveGatewayXMLConverter{

	@Override
	protected boolean writeExtensionChildElements(BaseElement element, boolean didWriteExtensionStartElement,
			XMLStreamWriter xtw) throws Exception {
		// TODO Auto-generated method stub
		for (List<ExtensionAttribute> attributes : element.getAttributes().values()) {

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
