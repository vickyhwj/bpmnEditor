package com.activiti6.util;

public class Result {
	String code="SUCCESS";
	
	public Result success(){
		this.code="SUCCESS";
		return this;
	}
	
	public Result fail(){
		this.code="FAILURE";
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
