package com.activiti6.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class TaskRecord {
	Long id;
	String taskId;
	String processInstanceId;
	String taskName;
	String nextFlowId;
	String nextFlowName;
	String nextNodeId;
	String nextNodeName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getNextFlowId() {
		return nextFlowId;
	}
	public void setNextFlowId(String nextFlowId) {
		this.nextFlowId = nextFlowId;
	}
	public String getNextFlowName() {
		return nextFlowName;
	}
	public void setNextFlowName(String nextFlowName) {
		this.nextFlowName = nextFlowName;
	}
	public String getNextNodeId() {
		return nextNodeId;
	}
	public void setNextNodeId(String nextNodeId) {
		this.nextNodeId = nextNodeId;
	}
	public String getNextNodeName() {
		return nextNodeName;
	}
	public void setNextNodeName(String nextNodeName) {
		this.nextNodeName = nextNodeName;
	}
	
	
}
