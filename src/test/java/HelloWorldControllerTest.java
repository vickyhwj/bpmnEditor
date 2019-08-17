import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntityImpl;
import org.activiti.engine.impl.persistence.entity.MembershipEntity;
import org.activiti.engine.impl.persistence.entity.MembershipEntityImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.InterpolationTermState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import com.activiti6.Activiti6DemoApplication;
import com.activiti6.service.ActivitiService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Activiti6DemoApplication.class)
public class HelloWorldControllerTest {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	IdentityService identityService;
	@Autowired
	HistoryService historyService;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	ActivitiService activitiService;
	
	@Test
	public void t2(){
		Map<String,Object> param=new HashMap<>();
//		List<String> users=new ArrayList<String>();
//		users.add("1");
//		users.add("2");
//		param.put("users",users );
	   runtimeService.startProcessInstanceByKey("redispro",param);
	}
	
	@Test
	public void addCandidate(){
//		for(int i=0;i<=5;++i){
//			Group group= identityService.newGroup(String.valueOf(i));
//		
//			identityService.saveGroup(group);
//		}
//		for(int i=0;i<10;++i)
//		{
//			User user=identityService.newUser(String.valueOf(i));
//			identityService.saveUser(user);
//
//		}
		
		
//		identityService.createMembership("1", "1");
//		identityService.createMembership("2", "2");
//		identityService.createMembership("3", "3");
		identityService.createMembership("5", "2");
	
	
	}
	
	
	@Test
	public void findBycandidate(){
		Map<String,Object> param=new HashMap<>();
//		List<String> users=new ArrayList<String>();
//		users.add("1");
//		users.add("2");
//		param.put("users",users );
		Task task= taskService.createTaskQuery().taskCandidateOrAssigned("5").singleResult();
		param.put("zkType", "省级智控");
		taskService.setAssignee(task.getId(), "2");
		taskService.complete(task.getId(), param);
	}
	
	@Test
	public void t3(){
		Task task= taskService.createTaskQuery().singleResult();
		Map<String,Object> map=new HashMap<>();
		map.put("path", "t22");
		taskService.complete(task.getId(),map);
	}
	
	@Test
	public void finishTask(){
		List<ProcessInstance> processInstances= runtimeService.createProcessInstanceQuery().list();
		for(ProcessInstance instance:processInstances)
			if(!instance.isEnded()){
				runtimeService.deleteProcessInstance(instance.getId(), "end");
	
			}
	}
	@Test
	public void getXML() throws IOException{
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
	            .processDefinitionId("redispro:4:182504").singleResult();

	    String resourceNmae=processDefinition.getResourceName();
	    
	    InputStream is=repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
	            resourceNmae);
	    
	    String str=IOUtils.toString(is, "UTF-8");
	    System.out.println(str);
	
		
	}
	
	@Test
	public void writeDia() throws FileNotFoundException, IOException{
		InputStream is= generateProcessDiagram("160001");
		StreamUtils.copy(is, new FileOutputStream(new File("D:/dia.png")));
		
	}
	
	private  InputStream generateProcessDiagram( String processInstanceId) {
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
		historicActivityInstanceList.forEach(historicActivityInstance->{executedActivityIdList.add(historicActivityInstance.getActivityId());});
		
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionEntity.getId());
		//已执行flow的集和
		List<String> executedFlowIdList = executedFlowIdList(bpmnModel,processDefinitionEntity,historicActivityInstanceList);
		
		ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
		InputStream diagram = processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,executedFlowIdList,"WenQuanYi Micro Hei","WenQuanYi Micro Hei", "WenQuanYi Micro Hei",
                null, 1.0);
		return diagram;
	}
	private static List<String> executedFlowIdList(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstanceList) {
		
		List<String> executedFlowIdList = new ArrayList<>();
		
	
		for(int i=0;i<historicActivityInstanceList.size()-1;i++) {
			HistoricActivityInstance hai = historicActivityInstanceList.get(i);
			FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(hai.getActivityId());
			List<SequenceFlow> sequenceFlows = flowNode.getOutgoingFlows();
			if(sequenceFlows.size()>1) {
				HistoricActivityInstance nextHai = historicActivityInstanceList.get(i+1);
				sequenceFlows.forEach(sequenceFlow->{
					if(sequenceFlow.getTargetFlowElement().getId().equals(nextHai.getActivityId())) {
						executedFlowIdList.add(sequenceFlow.getId());
					}
				});
			}else {
				executedFlowIdList.add(sequenceFlows.get(0).getId());
			}
		}
		
		return executedFlowIdList;
	}

	@Test
	public void viewTask(){
		UserTask userTask= activitiService.getUserTask("195005");
		userTask.getAttributes();
		System.out.println(userTask);
	}


}
