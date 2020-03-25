package com.activiti6.controller.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverterUtil;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.activiti6.service.ModelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 流程信息入库 liuzhize 2019年3月7日下午3:32:32
 */
@RestController
@RequestMapping("service")
public class ModelSaveRestResource implements ModelDataJsonConstants {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	ModelService modelService;
	
	
	
	
	@RequestMapping(value = "/model/save")
	@ResponseStatus(value = HttpStatus.OK)
	public void saveModel111( String modelId, String name, String description, String json_xml,
			String svg_xml) {
		saveModel(modelId, name, description, json_xml, svg_xml);
	}

	
	@RequestMapping(value = "/model/toUpdate")
	public ModelAndView toUpdate(ModelAndView modelAndView,String modelId) throws UnsupportedEncodingException {
		Model model= repositoryService.createModelQuery().modelId(modelId).singleResult();
		
		modelAndView.addObject("modelId", modelId);
		modelAndView.addObject("name",model.getName());

		modelAndView.addObject("json_xml", new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
		
//		byte[] svg_byte= repositoryService.getModelEditorSourceExtra(model.getId());
//		
//		InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes("utf-8"));
//		TranscoderInput input = new TranscoderInput(svgStream);
//
//		PNGTranscoder transcoder = new PNGTranscoder();
//		// Setup output
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		TranscoderOutput output = new TranscoderOutput(outStream);
//		
//		// Do the transformation
//		transcoder.transcode(input, output);
//		final byte[] result = outStream.toByteArray();
//		repositoryService.addModelEditorSourceExtra(model.getId(), result);
//		
//		modelAndView.addObject("name",model.getName());
		
        modelAndView.setViewName("modelUpdate");
        return modelAndView;
	}
	
	/**
	 * 保存流程
	 * 
	 * @param modelId1
	 *            模型ID
	 * @param name
	 *            流程模型名称
	 * @param description
	 * @param json_xml
	 *            流程文件
	 * @param svg_xml
	 *            图片
	 */
	@RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void saveModel(@PathVariable String modelId, String name, String description, String json_xml,
			String svg_xml) {
		try {

			modelService.saveModel(modelId, name, description, json_xml, svg_xml);
		} catch (Exception e) {
			LOGGER.error("Error saving model", e);
			throw new ActivitiException("Error saving model", e);
		}
	}

	private String doFilter(String json_xml) throws JsonProcessingException, IOException {
		// TODO Auto-generated method stub
		JsonNode jsonObject = objectMapper.readTree(json_xml);
		ArrayNode childShapes = (ArrayNode) jsonObject.get(BpmnJsonConverterUtil.EDITOR_CHILD_SHAPES);
		for (int i = 0; i < childShapes.size(); ++i) {
			JsonNode jsonNode = childShapes.get(i);
			System.out.println(BpmnJsonConverterUtil.getStencilId(jsonNode));
			if (BpmnJsonConverterUtil.STENCIL_TASK_USER.equals(BpmnJsonConverterUtil.getStencilId(jsonNode))) {
				JsonNode property = jsonNode.get("properties");
				String mulCollection = BpmnJsonConverterUtil
						.getPropertyValueAsString(BpmnJsonConverterUtil.PROPERTY_MULTIINSTANCE_COLLECTION, jsonNode);
				if (StringUtils.hasLength(mulCollection)) {
					((ObjectNode) property).put("multiinstance_variable", "user");
					setAssignee("${user}",jsonNode,(ObjectNode) property);
				}
			}
		}

		return jsonObject.toString();
	}

	private void setAssignee(String assignee, JsonNode taskNode,ObjectNode properties) {
		// TODO Auto-generated method stub
		ObjectNode usertaskassignment= getObjectNodeAndCreate ( properties,"usertaskassignment") ;
	
		
		ObjectNode assginmentNode= getObjectNodeAndCreate( usertaskassignment,"assignment");
		
		assginmentNode.put("assignee", assignee);
		
	}
	
	private ObjectNode getObjectNodeAndCreate(ObjectNode parent,String key){
		JsonNode jsonNode= parent.get(key);
		if(!(jsonNode instanceof ObjectNode)){
			jsonNode=objectMapper.createObjectNode();
			parent.put(key, jsonNode);
		}
		return (ObjectNode) jsonNode;
	}

}
