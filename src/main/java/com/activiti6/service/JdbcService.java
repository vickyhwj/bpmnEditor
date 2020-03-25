package com.activiti6.service;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.activiti6.entity.TaskRecord;
import com.activiti6.util.JdbcUtils;

@Service
public class JdbcService {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int insert(Object object) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		String tableName= JdbcUtils.beanName2LineName( object.getClass().getSimpleName());
		PropertyDescriptor[] pds= BeanUtils.getPropertyDescriptors(object.getClass());
//		jdbcTemplate.update("insert into "+tableName)
		Field[] fields= object.getClass().getDeclaredFields();
		Object[] params=new Object[fields.length];
		StringBuffer sql=new StringBuffer("insert into "+tableName+" ( ");
		StringBuffer values=new StringBuffer(" values ( ");
		for(int i=0;i<fields.length;++i){
			sql.append(JdbcUtils.beanName2LineName(fields[i].getName()));
			values.append("?");
			if(i<fields.length-1){
				sql.append(",");
				values.append(",");
			}
			params[i]=org.apache.commons.beanutils.BeanUtils.getProperty(object, fields[i].getName());
		}
		sql.append(")");
		values.append(")");
		return jdbcTemplate.update(sql.toString()+values.toString(),params);
	}
	
	public int delete(Class clazz,Serializable id){
		return jdbcTemplate.update("delete from "+JdbcUtils.beanName2LineName(clazz.getSimpleName())+ " where id=?",id);
	}
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		TaskRecord taskRecord=new TaskRecord();
		taskRecord.setId(1L);
		new JdbcService().insert(taskRecord);
	}

}
