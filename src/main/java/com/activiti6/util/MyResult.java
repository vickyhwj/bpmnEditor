package com.activiti6.util;

public class MyResult {
	boolean success=true;
	String msg;
	Object obj;
	
	
	
	public MyResult(boolean success, String msg, Object obj) {
		super();
		this.success = success;
		this.msg = msg;
		this.obj = obj;
	}



	public boolean isSuccess() {
		return success;
	}



	public void setSuccess(boolean success) {
		this.success = success;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public Object getObj() {
		return obj;
	}



	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
