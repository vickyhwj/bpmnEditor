import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
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
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.impl.persistence.entity.UserEntityImpl;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import com.activiti6.Activiti6DemoApplication;
import com.activiti6.cmd.JumpActivitiCmd;
import com.activiti6.entity.NextNode;
import com.activiti6.service.ActivitiService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Activiti6DemoApplication.class)
public class HelloWorldControllerTest {
	@Autowired
	JdbcTemplate jdbcTemplate;
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
		Map<String,Object> ap=new HashMap<>();
		ap.put("afP_user","hwj");
//		param.put("can", Arrays.asList("1"));
//		param.put("aform_assignee", "a1");
//		ap.put("afP_husers", "b1,b2,b3");
		
		Map<String,Object> at=new HashMap<>();
		
	   runtimeService.createProcessInstanceBuilder().transientVariables(at).variables(ap).processDefinitionKey("test_back").start();
	}
	@Test
	public void runAndSet() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Map<String,Object> ap=new HashMap<>();
//		ap.put("afP_user","yes");
	
		Map<String,Object> at=new HashMap<>();
		
		activitiService.runAndClaim("695007", null,null,ap);
	}
	@Test
	public void assignee() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		List<NextNode> nextNodes= activitiService.getNextNodes("582505");
		System.out.println(nextNodes);
	}
	
	@Test
	public void complete(){
		
		Map<String,Object> param=new HashMap<>();
//		param.put("users", Arrays.asList("2","3"));
		Task task= taskService.createTaskQuery().taskId("607516").singleResult();
		
		
		taskService.complete(task.getId(),null,param);
		
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
//		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//	            .processDefinitionId("redispro:11:190012").singleResult();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("t2").latestVersion() .singleResult();

	    String resourceNmae=processDefinition.getResourceName();
	    
	    InputStream is=repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
	            resourceNmae);
	    
	    String str=IOUtils.toString(is, "UTF-8");
	    System.out.println(str);
	
		
	}
	
	@Test
	public void writeDia() throws FileNotFoundException, IOException{
		InputStream is= generateProcessDiagram("537501");
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
	private List<String> getActRuTaskActIds(String processInstanceId) {
		List<String> actIds=new ArrayList<>();
		List<Task> tasks= taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		for(Task task:tasks){
			actIds.add(((TaskEntityImpl)task).getTaskDefinitionKey());
		}
		return actIds ;
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
		UserTask userTask= activitiService.getUserTask("237505");
		userTask.getAttributes();
		List<ExtensionElement> es= userTask.getExtensionElements().get("extenison");
		System.out.println(userTask);
	}
	
	@Test
	public void getNextAfterStart() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		List<NextNode> nextNodes= activitiService.getNextNodesAfterStart("test_back");
		System.out.println(nextNodes);
	}

	@Test
	public void getNextNodeByTaskId() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		List<NextNode> nextNodes=activitiService.getNextNodes("697502");
		System.out.println(nextNodes);
	}
	
	@Test
	public void doNext(){
		String taskId="552505";
		Task task= taskService.createTaskQuery().taskId(taskId).singleResult();
		taskService.addComment(taskId, task.getProcessInstanceId(), "fuck you");
		Map<String,Object> param=new HashMap<>();
		param.put(NextNode.assign_assignee,"a1");
		taskService.complete(taskId,null, param);
	}
	
	@Test
	public void jump(){
		String taskId="297505";
		String targetNodeId="sid-95F02785-5D04-4089-8A48-7305052BE191";
		
		processEngine.getManagementService().executeCommand(new JumpActivitiCmd(taskId, targetNodeId));
	}
	
	@Test
	public void createUsers(){
		identityService.deleteGroup("需求填报组");
		
		 Group group= identityService.newGroup("需求填报组");
		 group.setName("需求填报组");
		 identityService.saveGroup(group);
		 
		 User gdyxsh=identityService.newUser("gdyxsh");
		 identityService.saveUser( gdyxsh);
		 
		 User gdyxlr=identityService.newUser("gdyxlr");
		 identityService.saveUser( gdyxlr);
		 
		 identityService.createMembership(gdyxlr.getId(), group.getId());
		 identityService.createMembership(gdyxsh.getId(), group.getId());

	}
	
	@Test
	public void taskQuery(){
		taskService.createTaskQuery().taskCandidateOrAssigned("gdyxsh").list();
		System.out.println(taskService.createTaskQuery().taskCandidateUser("gdyxsh").list());
	}
	
	@Test
	public void chaim(){
		taskService.claim("367510", "gdyxsh");
	}
	
	@Test
	public void max(){
		
		jdbcTemplate.query("select * from act_hi_taskinst", new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
		
				RowMapper<HiTask> taMapper=new BeanPropertyRowMapper<>(HiTask.class);
				HiTask userTask=taMapper.mapRow(rs, 0);
				System.out.println(userTask);
			}
		}, new Object[]{});
		

	}
	public static class HiTask{
		String id_;
		String PROC_DEF_ID_;
		public String getId_() {
			return id_;
		}
		public void setId_(String id_) {
			this.id_ = id_;
		}
		public String getPROC_DEF_ID_() {
			return PROC_DEF_ID_;
		}
		public void setPROC_DEF_ID_(String pROC_DEF_ID_) {
			PROC_DEF_ID_ = pROC_DEF_ID_;
		}
		public HiTask() {
			super();
		}
		
	}
}
