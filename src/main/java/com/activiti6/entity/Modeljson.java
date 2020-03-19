package com.activiti6.entity;

public class Modeljson {
	Long id;
	String model_id;
	byte[] json;
	String deploy_id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getModel_id() {
		return model_id;
	}
	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}
	public byte[] getJson() {
		return json;
	}
	public void setJson(byte[] json) {
		this.json = json;
	}
	public String getDeploy_id() {
		return deploy_id;
	}
	public void setDeploy_id(String deploy_id) {
		this.deploy_id = deploy_id;
	}
	
	

}
