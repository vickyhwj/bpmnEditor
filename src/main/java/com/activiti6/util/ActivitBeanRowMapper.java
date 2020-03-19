package com.activiti6.util;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.activiti6.entity.ModelVo;

public class ActivitBeanRowMapper extends BeanPropertyRowMapper{

	public ActivitBeanRowMapper(Class<ModelVo> class1) {
		// TODO Auto-generated constructor stub
		super(class1);
	}

	@Override
	protected String underscoreName(String name) {
		// TODO Auto-generated method stub
		return super.underscoreName(name)+"_";
	}

	
	
}
