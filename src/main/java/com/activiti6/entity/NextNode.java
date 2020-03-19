package com.activiti6.entity;

import java.util.ArrayList;
import java.util.List;

public class NextNode {
	public final static String assign_assignee="assignee";
	public final static String assign_candidateUser="candidateUsers";
	public final static String assign_candidateGroup="candidateGroup";
	public final static String assign_parrallel="parrallel";
	public final static String assign_sequential="sequential";
	public final static String assign_exclusiveGateWay="exclusiveGateWay";

	String id;
	String seqFlowName;
	Integer order;
	String taskId;
	String taskName;
	List<User> user=new ArrayList<User>();
	String assignType;
	List<NextNode> nextNodes=new ArrayList<NextNode>();
	
	public String getSeqFlowName() {
		return seqFlowName;
	}
	public void setSeqFlowName(String seqFlowName) {
		this.seqFlowName = seqFlowName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public List<User> getUser() {
		return user;
	}
	public void setUser(List<User> user) {
		this.user = user;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getAssignType() {
		return assignType;
	}
	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}
	public List<NextNode> getNextNodes() {
		return nextNodes;
	}
	public void setNextNodes(List<NextNode> nextNodes) {
		this.nextNodes = nextNodes;
	}
	
	

}
