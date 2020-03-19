package com.activiti6.config.handler;

import org.activiti.bpmn.model.UserTask;

import com.fasterxml.jackson.databind.JsonNode;

public interface JSONHandler {

	String getFieldName();

	void handle(JsonNode node, UserTask flowElement);

}
