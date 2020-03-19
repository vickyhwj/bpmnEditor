package com.activiti6.service;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.collections.MapUtils;
import org.mozilla.javascript.ObjArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activiti6.entity.Modeljson;

@Service
public class ModelJsonService {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void insertModelJson(String modelId,byte[] json,String deploy_id) throws Exception{
		
		jdbcTemplate.update("insert into modeljson(model_id,json,deploy_id) values(?,?,?)",modelId,new SerialBlob(json),deploy_id);
		
			        
	}
	
	public Long findMaxIdOnModel(String modelId){
	
		return jdbcTemplate.queryForObject("select max(id) from modeljson where model_id=?",new Object[]{ modelId},Long.class);
	}
	
	public void deleteById(Long id){
		jdbcTemplate.update("delete from modeljson where id=?", id);
	}
	
	public void deleteByModelId(String modelId){
		jdbcTemplate.update("delete from modeljson where model_id=?",modelId);
	}
	
	public void deleteByMaxId(String modelId){
		Long id=findMaxIdOnModel(modelId);
		if(id!=null){
			deleteById(id);
		}
	}
	
	public Modeljson getById(Long id){
		return (Modeljson) jdbcTemplate.queryForObject ("select * from modeljson where id=?",new Object[]{id},new BeanPropertyRowMapper(Modeljson.class));
	}
	
	public List<Modeljson> findModelJsonsByModelId(String modelId){
		return jdbcTemplate.query  ("select * from modeljson where model_id=?", new Object[]{modelId},new BeanPropertyRowMapper(Modeljson.class));
	}
	
	@Transactional
	public void deleteAndReconvertLastVersion(String modelId) throws SerialException, SQLException{
		deleteByMaxId(modelId);
		Long maxId=findMaxIdOnModel(modelId);
		Modeljson res= getById(maxId);
		Blob blod=new SerialBlob( res.getJson());
		String updateSql="update act_ge_bytearray set BYTES_=? where  ID_=(select EDITOR_SOURCE_VALUE_ID_  from act_re_model where id_=?) and NAME_='source'";
		jdbcTemplate.update(updateSql,blod,modelId );
		
		jdbcTemplate.update("update act_re_model set DEPLOYMENT_ID_=? where ID_=?",res.getDeploy_id(),modelId);
	}
}
