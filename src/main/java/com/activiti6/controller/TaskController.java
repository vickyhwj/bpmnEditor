package com.activiti6.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.activiti6.entity.TaskRecord;
import com.activiti6.service.ActivitiService;
import com.activiti6.util.ActivitiContext;
import com.activiti6.util.ActivitiContextHolder;
import com.activiti6.util.MyResult;
import com.activiti6.util.RequestUtil;

@Controller
public class TaskController extends BaseController{
	
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ActivitiService activitiService;
	
	@Autowired
	RuntimeService runtimeService;
	
	
	@ResponseBody
	@RequestMapping("/claim")
	public MyResult claim(String taskId){
		try{
			String userId="";
			taskService.claim(taskId, userId);
			return new MyResult(true, null, null);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return new MyResult(false, e.getMessage(), e);
		}
	}
	
	@ResponseBody
	@RequestMapping("/complete")
	public MyResult complete(TaskRecord taskRecord,HttpServletRequest request){
		try{
			
			Map<String,Object> afP=RequestUtil.getafPParam(request);
			Map<String,Object> afT=RequestUtil.getafTParam(request);
			Map<String,Object> afL=RequestUtil.getafLParam(request);
			activitiService.runAndClaim(taskRecord, taskRecord.getNextFlowId(), afL, afT, afP);
			return new MyResult(true, null, ActivitiContextHolder.getContext());
			
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return new MyResult(false, e.getMessage(), e);
		}
	}

	
	@ResponseBody
	@RequestMapping("/start")
	public MyResult start(String processKey,HttpServletRequest request){
		try{
			
			Map<String,Object> afP=RequestUtil.getafPParam(request);
			Map<String,Object> afT=RequestUtil.getafTParam(request);

			runtimeService.createProcessInstanceBuilder().transientVariables(afT).variables(afP).processDefinitionKey(processKey).start();

			return new MyResult(true, null, ActivitiContextHolder.getContext());
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return new MyResult(false, e.getMessage(), e);
		}
	}

	
	@ResponseBody
	@RequestMapping("/getNextNodesByTaskId")
	public MyResult getNextNodesByTaskId(String taskId){
		try{
			return new MyResult(true, null, activitiService.getNextNodes(taskId));
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return new MyResult(false, e.getMessage(), e);
		}
	}
	
	@ResponseBody
	@RequestMapping("/getNextNodesAfterStart")
	public MyResult getNextNodesAfterStart(String processKey){
		try{
			return new MyResult(true, null, activitiService.getNextNodesAfterStart(processKey));
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return new MyResult(false, e.getMessage(), e);
		}
	}
}
