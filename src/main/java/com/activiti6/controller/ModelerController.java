package com.activiti6.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.editor.language.json.converter.BpmnJsonConverterUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.activiti6.config.CustomBpmnJsonConverter;
import com.activiti6.config.CustomExclusiveGatewayJsonConverter;
import com.activiti6.config.CustomUserTaskJsonConverter;
import com.activiti6.config.SeqFlowJsonConverter;
import com.activiti6.entity.ModelVo;
import com.activiti6.entity.Modeljson;
import com.activiti6.service.ModelJsonService;
import com.activiti6.service.ModelService;
import com.activiti6.util.ActivitBeanRowMapper;
import com.activiti6.util.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.mail.handlers.message_rfc822;

/**
 * 流程控制器
 * liuzhize 2019年3月7日下午3:28:14
 */
@Controller
public class ModelerController{

    private static final Logger logger = LoggerFactory.getLogger(ModelerController.class);

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService ;
    
    @Autowired
    ModelJsonService modelJsonService;
    
    @Autowired 
    ModelService modelService;

    @Autowired
    JdbcTemplate jdbcTemplate;
    
	@RequestMapping("index")
	public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
//        modelAndView.addObject("modelList", repositoryService.createModelQuery().list());
        String sql="select ID_, REV_, NAME_, KEY_, CATEGORY_, CREATE_TIME_, LAST_UPDATE_TIME_,  META_INFO_, DEPLOYMENT_ID_, EDITOR_SOURCE_VALUE_ID_, EDITOR_SOURCE_EXTRA_VALUE_ID_, TENANT_ID_,VERSION_,(select count(1) from modeljson where model_id=id_ ) jversion from act_re_model m";
//        modelAndView.addObject("modelList", repositoryService.createNativeModelQuery().sql(sql).list());
        modelAndView.addObject("modelList", jdbcTemplate.query(sql,new Object[]{}, new ActivitBeanRowMapper(ModelVo.class)));
        return modelAndView;
	}
	
    /**
     * 跳转编辑器页面
     * @return
     */
    @GetMapping("editor")
    public String editor(){
        return "modeler";
    }
    
    
    /**
     * 创建模型
     * @param response
     * @param name 模型名称
     * @param key 模型key
     */
    @RequestMapping("/create")
    public void create(HttpServletResponse response,String name,String key) throws IOException {
    	logger.info("创建模型入参name：{},key:{}",name,key);
        Model model = repositoryService.newModel();
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);
        createObjectNode(model.getId());
        response.sendRedirect("/editor?modelId="+ model.getId());
        logger.info("创建模型结束，返回模型ID：{}",model.getId());
    }
    
    /**
     * 创建模型时完善ModelEditorSource
     * @param modelId
     */
	@SuppressWarnings("deprecation")
	private void createObjectNode(String modelId){
    	 logger.info("创建模型完善ModelEditorSource入参模型ID：{}",modelId);
    	 ObjectNode editorNode = objectMapper.createObjectNode();
         editorNode.put("id", "canvas");
         editorNode.put("resourceId", "canvas");
         ObjectNode stencilSetNode = objectMapper.createObjectNode();
         stencilSetNode.put("namespace","http://b3mn.org/stencilset/bpmn2.0#");
         editorNode.put("stencilset", stencilSetNode);
         try {
			repositoryService.addModelEditorSource(modelId,editorNode.toString().getBytes("utf-8"));
		} catch (Exception e) {
			 logger.info("创建模型时完善ModelEditorSource服务异常：{}",e);
		}
        logger.info("创建模型完善ModelEditorSource结束");
    }
    
    /**
     * 发布流程
     * @param modelId 模型ID
     * @return
     */
    @ResponseBody
    @RequestMapping("/publish")
    public Result publish(String modelId){
    	try{
    		modelService.publish(modelId);
    		return new Result().success(); 
    	}catch(Exception e){
    		logger.error("发布流程失败", e);
    		return new Result().fail();
    	}
    }
    
    /**
     * 撤销流程定义1
     * @param modelId 模型ID
     * @param result
     * @return
     */
    @ResponseBody
    @RequestMapping("/revokePublish")
    public Object revokePublish(String modelId){
    	try{
    		modelService.revokePublish(modelId);
    		return new Result().success();
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    		return new Result().fail();
    	}
    }
    
    /**
     * 删除流程实例
     * @param modelId 模型ID
     * @param result
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Object deleteProcessInstance(String modelId){
    	try{
    		modelService.deleteModel(modelId);
    		return new Result().success();
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    		return new Result().fail();
    	}
    }
}
