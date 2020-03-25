package com.activiti6.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialException;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.activiti6.config.CustomBpmnJsonConverter;
import com.activiti6.config.CustomExclusiveGatewayJsonConverter;
import com.activiti6.config.CustomUserTaskJsonConverter;
import com.activiti6.config.SeqFlowJsonConverter;
import com.activiti6.controller.ModelerController;
import com.activiti6.entity.Modeljson;
import com.activiti6.mapper.TaskRecordMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.mail.imap.protocol.MODSEQ;

@Service
public class ModelService {

	private static final Logger logger = LoggerFactory.getLogger(ModelerController.class);

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	ModelJsonService modelJsonService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	TaskRecordMapper taskRecordMapper;
	

	@Transactional
	public void publish(String modelId) throws Exception {

		logger.info("流程部署入参modelId：{}", modelId);
		Model modelData = repositoryService.getModel(modelId);
		byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
		if (bytes == null) {
			throw new RuntimeException("部署ID:" + modelId + "的模型数据为空，请先设计流程并成功保存，再进行发布");
			// map.put("code", "FAILURE");
			// return map;
		}

		// TODO:UserTask自定义扩展属性
		CustomBpmnJsonConverter.getConvertersToBpmnMap().put("UserTask", CustomUserTaskJsonConverter.class);
		CustomBpmnJsonConverter.getConvertersToBpmnMap().put("SequenceFlow", SeqFlowJsonConverter.class);
		CustomBpmnJsonConverter.getConvertersToBpmnMap().put("ExclusiveGateway",
				CustomExclusiveGatewayJsonConverter.class);

		BpmnJsonConverter bpmnJsonConverter = new CustomBpmnJsonConverter();

		JsonNode modelNode = new ObjectMapper().readTree(bytes);
		BpmnModel model = bpmnJsonConverter.convertToBpmnModel(modelNode);
		System.out.println(new BpmnJsonConverter().convertToJson(model).toString());
		Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
				.addBpmnModel(modelData.getKey() + ".bpmn20.xml", model).deploy();
		modelData.setDeploymentId(deployment.getId());
		modelData.setVersion(1);
		repositoryService.saveModel(modelData);

		modelJsonService.insertModelJson(modelId, bytes, deployment.getId());

	}

	@Transactional
	public void revokePublish(String modelId) throws SerialException, SQLException {

		logger.info("撤销发布流程入参modelId：{}", modelId);
		Model modelData = repositoryService.getModel(modelId);
		if (null != modelData) {
			/**
			 * 参数不加true:为普通删除，如果当前规则下有正在执行的流程，则抛异常
			 * 参数加true:为级联删除,会删除和当前规则相关的所有信息，包括历史
			 */
			repositoryService.deleteDeployment(modelData.getDeploymentId(), true);

			modelJsonService.deleteAndReconvertLastVersion(modelId);
			modelData.setVersion(1);
			repositoryService.saveModel(modelData);
		}

	}

	@Transactional
	public void deleteModel(String modelId) {

		logger.info("删除流程实例入参modelId：{}", modelId);
		Map<String, String> map = new HashMap<String, String>();
		Model modelData = repositoryService.getModel(modelId);
		if (null != modelData) {
			List<Modeljson> jsons = modelJsonService.findModelJsonsByModelId(modelId);
			for (Modeljson json : jsons) {
//				jdbcTemplate.update("delete from task_record where process_instance_id in(select ahp.ID_ from act_hi_procinst ahp join act_re_procdef arp on arp.ID_=ahp.PROC_DEF_ID_ where arp.DEPLOYMENT_ID_=?)",json.getDeploy_id());
				taskRecordMapper.deleteByDeploymentId(json.getDeploy_id());
				repositoryService.deleteDeployment(json.getDeploy_id(), true);
				
				jdbcTemplate.update("delete from act_re_procdef where DEPLOYMENT_ID_=?", json.getDeploy_id());
				modelJsonService.deleteById(json.getId());
			}
			
//			jdbcTemplate.update("delete from act_re_model where id_=?",modelId);
			repositoryService.deleteModel(modelId);

		}

	}
	@Transactional
	public void saveModel(@PathVariable String modelId, String name, String description, String json_xml,
			String svg_xml) throws Exception {
		

			Model model = repositoryService.getModel(modelId);

			ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

//			json_xml = doFilter(json_xml);

			modelJson.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			model.setMetaInfo(modelJson.toString());
			model.setName(name);
			model.setVersion(0);
			
			net.sf.json.JSONObject json=net.sf.json.JSONObject.fromObject(json_xml);
			String process_id= json.getJSONObject("properties").getString("process_id");
			model.setKey(process_id);
			
			repositoryService.saveModel(model);

			repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes("utf-8"));
/*
			InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
			TranscoderInput input = new TranscoderInput(svgStream);

			PNGTranscoder transcoder = new PNGTranscoder();
			// Setup output
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(outStream);

			// Do the transformation
			transcoder.transcode(input, output);
			final byte[] result = outStream.toByteArray();
			repositoryService.addModelEditorSourceExtra(model.getId(), result);
			outStream.close();*/
	}
}
