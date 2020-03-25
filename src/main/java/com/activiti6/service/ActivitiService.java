package com.activiti6.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.catalina.valves.ExtendedAccessLogValve;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import com.activiti6.config.ExtAttrKeys;
import com.activiti6.entity.NextNode;
import com.activiti6.entity.TaskRecord;
import com.activiti6.entity.User;
import com.activiti6.mapper.TaskRecordMapper;
import com.activiti6.util.BpmnUtils;

import listener.ExclusiveGatewayChooseHandler;
import listener.TaskChooseHandler;
@Service
public class ActivitiService {
	@Autowired
	TaskService taskService;
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	IdentityService identityService;
	@Autowired
	HistoryService historyService;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	TaskRecordMapper taskRecordMapper;
	
	
	public void findHistoryAssgine(){
		HistoricTaskInstance hisTask= historyService.createHistoricTaskInstanceQuery().singleResult();
		List<HistoricIdentityLink> identityLinks= historyService.getHistoricIdentityLinksForTask("1");
//		IdentityLinkType. identityLinks.get(0).getType()
	}
	
	
	public org.activiti.bpmn.model.Process getProcessByTaskId(String taskId){
		TaskEntityImpl taskEntityImpl=(TaskEntityImpl) taskService.createTaskQuery().taskId(taskId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(taskEntityImpl.getProcessDefinitionId());
		org.activiti.bpmn.model.Process process=bpmnModel.getProcesses().get(0);
		return process;
	}
	
	public UserTask getUserTask(String taskDefinitionKey,String processDefinitionId){
		
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		org.activiti.bpmn.model.Process process=bpmnModel.getProcesses().get(0);
		process.getFlowElement(taskDefinitionKey);
		return (UserTask) process.getFlowElement(taskDefinitionKey);
		
	}
	
	/**
	 * 在任务监听器不能用这方法，因为因为任务还没有创建保存到db
	 * @param taskId
	 * @return
	 */
	public UserTask getUserTask(String taskId){
		TaskEntityImpl taskEntityImpl=(TaskEntityImpl) taskService.createTaskQuery().taskId(taskId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(taskEntityImpl.getProcessDefinitionId());
		org.activiti.bpmn.model.Process process=bpmnModel.getProcesses().get(0);
		List<UserTask> userTaskList = process.findFlowElementsOfType(UserTask.class);
		for(int i=0;i<userTaskList.size();++i){
			if(taskEntityImpl.getTaskDefinitionKey().equals(userTaskList.get(i).getId())){
				return userTaskList.get(i);
			}
		}
		return null;
		
	}
	public static void main(String[] args) {
		Matcher mat=Pattern.compile("\\$\\{\\s*([^\\s]*)\\s*\\}").matcher("${  wwe  }");
		System.out.println(mat.matches()); 
		System.out.println(mat.group(1)+"ss");
		 
	}
	
	public void getUserOnUserTask(UserTask task,NextNode nextNode){
		
		if(StringUtils.hasLength(task.getAssignee())){
			Matcher mat=Pattern.compile("\\$\\{\\s*([^\\s]*)\\s*\\}").matcher(task.getAssignee().trim());
			if(!mat.matches()){
				List<User> list=new ArrayList<>();
				list.add(new User(task.getAssignee().trim()));
				nextNode.setUser(list);
				nextNode.setAssignType(NextNode.assign_assignee);
			}
			else{
				if(NextNode.assign_assignee.equals(mat.group(1))){
					
				}
			}
		}
		else if(!CollectionUtils.isEmpty(task.getCandidateUsers())){
			Matcher mat=Pattern.compile("\\$\\{\\s*([^\\s]*)\\s*\\}").matcher(task.getCandidateUsers().get(0).trim());
			if(!mat.matches()){
				List<User> list=new ArrayList<>();
				for(String can:task.getCandidateUsers()){
					list.add(new User(can));
				}
				nextNode.setAssignType(NextNode.assign_candidateUser);
				nextNode.setUser(list);
			}

		}
		else if(task.getBehavior() instanceof SequentialMultiInstanceBehavior){
			SequentialMultiInstanceBehavior behavior=(SequentialMultiInstanceBehavior) task.getBehavior();
		}
		
		
	}
	public List<NextNode> getNextNodesBySeqs(List<SequenceFlow> sequenceFlows) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		List<NextNode> nextNodes=new ArrayList<NextNode>();
		for(int i=0;i<sequenceFlows.size();++i){
			SequenceFlow outFlow= sequenceFlows.get(i);
			Map<String,String> seqFlowExtmap= BpmnUtils.getExtensionMap(outFlow);

			
			NextNode nextNode=new NextNode();
			nextNode.setId(outFlow.getId());
			nextNode.setSeqFlowName(outFlow.getName());
			nextNode.setOrder((Integer) ConvertUtils.convert(seqFlowExtmap.get(ExtAttrKeys.seq_order), Integer.class));
			FlowElement flowElement=outFlow.getTargetFlowElement();
			if(flowElement instanceof UserTask){
				UserTask task=(UserTask) flowElement;
				Map<String,String> extensionMap= BpmnUtils.getExtensionMap(task);
				if(StringUtils.hasLength(extensionMap.get(ExtAttrKeys.userTask_chooseClass))){
					 String handlerStr=extensionMap.get(ExtAttrKeys.userTask_chooseClass);
					 String className=handlerStr;
					 String[] param=new String[0];
					 if(handlerStr.contains("(")){
						 String paramStr=handlerStr.substring(handlerStr.indexOf("(")+1, handlerStr.indexOf(")")).trim();
						 param=paramStr.split(",");
						 className=handlerStr.substring(0, handlerStr.indexOf("("));
					 }
					 
					 TaskChooseHandler handler= (TaskChooseHandler) Class.forName(className).newInstance();
					 NextNode node= handler.getNextNode(outFlow,task, nextNode,param);
					 if(node!=null){
						 nextNodes.add(node);
					 }
				}
				else{
					nextNode.setTaskId(flowElement.getId());
					nextNode.setTaskName( flowElement.getName());
					getUserOnUserTask((UserTask) flowElement, nextNode);
					nextNodes.add(nextNode);
				}

			}
			else if(flowElement instanceof EndEvent){
				nextNode.setTaskId(flowElement.getId());
				nextNode.setTaskName(flowElement.getName());
				nextNode.setUser(new ArrayList<>());
				nextNodes.add(nextNode);
			}
			else if(flowElement instanceof ExclusiveGateway){
				Map<String,String> extMap=BpmnUtils.getExtensionMap(flowElement);
				String exclGateWay_chooseClassStr= extMap.get(ExtAttrKeys.exclGateWay_chooseClass);
				if(StringUtils.hasLength(exclGateWay_chooseClassStr)){
					String[] param=new String[0];
					String  exclGateWay_chooseClass=exclGateWay_chooseClassStr;
					if(exclGateWay_chooseClassStr.contains("(")){
						param=exclGateWay_chooseClassStr.substring(exclGateWay_chooseClassStr.indexOf("(")+1, exclGateWay_chooseClassStr.indexOf(")")).trim().split(",");
						exclGateWay_chooseClass=exclGateWay_chooseClassStr.substring(0, exclGateWay_chooseClassStr.indexOf("("));
					}
					ExclusiveGatewayChooseHandler handler= (ExclusiveGatewayChooseHandler) Class.forName(exclGateWay_chooseClass).newInstance();
					NextNode node= handler.getNextNode(outFlow,(ExclusiveGateway) flowElement, nextNode,param);
					if(node!=null){
						nextNodes.add(node);
					}
				}
				else{
					nextNode.setAssignType(NextNode.assign_exclusiveGateWay);
					nextNode.setTaskId(flowElement.getId());
					nextNode.setTaskName(flowElement.getName());
					nextNode.setNextNodes( getNextNodesBySeqs(((ExclusiveGateway)flowElement).getOutgoingFlows()));
					nextNodes.add(nextNode);
				}
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
				((ExclusiveGateway) flowElement).getOutgoingFlows().get(0).getConditionExpression();
			}
			else if(flowElement instanceof ParallelGateway){
				nextNode.setTaskName(flowElement.getName());
				nextNode.setUser(new ArrayList<>());
				nextNodes.add(nextNode);
			}
		}
		return nextNodes;
	}
	private String getAssignType(UserTask userTask) {
		if(userTask.getBehavior() instanceof ParallelMultiInstanceBehavior){
			return NextNode.assign_parrallel;
		}
		else if(userTask.getBehavior() instanceof SequentialMultiInstanceBehavior){
			return NextNode.assign_sequential;
		}
		else{
			if(!CollectionUtils.isEmpty(userTask.getCandidateUsers())){
				return NextNode.assign_candidateUser;
			}
			else if(!CollectionUtils.isEmpty(userTask.getCandidateGroups())){
				return NextNode.assign_candidateGroup;
			}
			
		}
	
		return NextNode.assign_assignee;
	}

	public List<NextNode> getNextNodes(String taskId) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		UserTask userTask=getUserTask(taskId);
		List<NextNode> nextNodes=getNextNodesBySeqs(userTask.getOutgoingFlows());
		
		return nextNodes;
				
		
	}
	
	public List<NextNode> getNextNodesAfterStart(String processKey) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		List<org.activiti.engine.repository.Deployment> deployments= repositoryService.createDeploymentQuery().deploymentKey(processKey).orderByDeploymenTime().desc().list();
		ProcessDefinition pd= repositoryService.createProcessDefinitionQuery().latestVersion().processDefinitionKey(processKey).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(pd.getId());
		org.activiti.bpmn.model.Process process=bpmnModel.getProcesses().get(0);
		List<StartEvent> startEvents=process.findFlowElementsOfType(StartEvent.class);
		List<NextNode> nextNodes=getNextNodesBySeqs(  startEvents.get(0).getOutgoingFlows());
		return nextNodes;
	}
//	public void rt(String taskId){
//		UserTask userTask=getUserTask(taskId);
//		List<ExtensionAttribute> extensionAttributes= userTask.getAttributes().getOrDefault(null, null);
//		extensionAttributes.get(0).get
//		List<SequenceFlow> osfs= userTask.getOutgoingFlows();
//		osfs.get(0).getTargetFlowElement()
//	}
	
	@Transactional
	public void runAndClaim(TaskRecord taskRecord,String nextFlowId,Map<String,Object> afLParam, Map<String,Object> afTParam,Map<String,Object> afPParam) throws Exception{
		TaskEntityImpl task= (TaskEntityImpl) taskService.createTaskQuery().taskId(taskRecord.getTaskId()).singleResult();
		if(afLParam!=null){
			taskService.setVariablesLocal(taskRecord.getTaskId(), afLParam);
		}
		List<NextNode> nextNodes= getNextNodes(taskRecord.getTaskId());
		String processInstanceId= task.getProcessInstanceId();
		if(StringUtils.hasLength(nextFlowId)){
			afPParam.put(ExtAttrKeys.nextNode, nextFlowId);
		}
		
//		if(NextNode.assign_assignee.equals(assignType)){
//			param.put(assignType, users.get(0));
//		}
//		else if(NextNode.assign_candidateGroup.equals(assignType)){
//			
//		}
//		else if(NextNode.assign_candidateUser.equals(assignType)){
//			param.put(assignType, users);
//		}
		
		taskRecord.setProcessInstanceId(task.getProcessInstanceId());
		taskRecord.setTaskName(task.getName());
		
		taskRecordMapper.insert(taskRecord);
		taskService.complete(taskRecord.getTaskId(),afPParam,afTParam);
//		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstanceId);
//		org.activiti.bpmn.model.Process process=bpmnModel.getProcesses().get(0);
//		List<UserTask> userTaskList = process.findFlowElementsOfType(UserTask.class);
	
	}
	
	private List<String> getActRuTaskActIds(String processInstanceId) {
		List<String> actIds=new ArrayList<>();
		List<Task> tasks= taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for(Task task:tasks){
			actIds.add(((TaskEntityImpl)task).getTaskDefinitionKey());
		}
		return actIds ;
	}
	
	public  InputStream generateProcessDiagram( String processInstanceId) {
		// TODO Auto-generated method stub
		//获取历史流程实例
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
		.processInstanceId(processInstanceId).singleResult();
		//获取历史流程定义
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(historicProcessInstance.getProcessDefinitionId());
		//查询历史节点
		List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
		.processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().asc().list();
		//已执行历史节点
		List<String> executedActivityIdList = new ArrayList<String>();
		executedActivityIdList.addAll(getActRuTaskActIds(processInstanceId));
//		historicActivityInstanceList.forEach(historicActivityInstance->{executedActivityIdList.add(historicActivityInstance.getActivityId());});
		
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionEntity.getId());
		//已执行flow的集和
//		List<String> executedFlowIdList = executedFlowIdList(bpmnModel,processDefinitionEntity,historicActivityInstanceList);
		List<String> executedFlowIdList=new ArrayList<>();
		
		ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
		InputStream diagram = processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,executedFlowIdList,"WenQuanYi Micro Hei","WenQuanYi Micro Hei", "WenQuanYi Micro Hei",
                null, 1.0);
		return diagram;
	}

}
