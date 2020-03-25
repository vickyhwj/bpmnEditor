package com.activiti6.util;

import java.util.ArrayList;
import java.util.List;

public class ActivitiContext {
	List<String> nextTaskIds=new ArrayList<>();

	public List<String> getNextTaskIds() {
		return nextTaskIds;
	}

	public void setNextTaskIds(List<String> nextTaskIds) {
		this.nextTaskIds = nextTaskIds;
	}
	
	
}
