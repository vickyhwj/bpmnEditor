package listener;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.activiti6.entity.NextNode;
import com.activiti6.entity.User;
import com.activiti6.util.BpmnUtils;
import com.activiti6.util.SpringUtil;

public abstract class TaskBackChooseHandlerImpl implements TaskChooseHandler{

	@Override
	public NextNode getNextNode(SequenceFlow inSequenceFlow,UserTask userTask, NextNode nextNode,String[] param) {
		
		if(isBackSeqFlow(inSequenceFlow)){
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
			String taskId= request.getParameter("taskId");
			TaskService taskService= SpringUtil.getApplicationContext().getBean(TaskService.class);
			Task curTask= taskService.createTaskQuery().taskId(taskId).singleResult();
			String lastAssignee=findLastAssginee(userTask.getId(),curTask.getProcessInstanceId());
			
			nextNode.setAssignType(NextNode.assign_assignee);
			nextNode.setTaskId(userTask.getId());
			nextNode.setTaskName(userTask.getName());
			nextNode.setUser(Arrays.asList(new User(lastAssignee)));
			return nextNode;
		}
		else {
			return getNextNodeNoBack( inSequenceFlow, userTask,  nextNode, param);
		}
	}
	/**
	 * 非退回
	 * @param inSequenceFlow
	 * @param userTask
	 * @param nextNode
	 * @param param
	 * @return
	 */
	public abstract  NextNode getNextNodeNoBack(SequenceFlow inSequenceFlow, UserTask userTask, NextNode nextNode,
			String[] param);

	private boolean isBackSeqFlow(SequenceFlow inSequenceFlow) {
		
		return inSequenceFlow.getId().startsWith("back_");
	}

	private String findLastAssginee(String taskDefKey, String processInstanceId) {
		HistoryService historyService= SpringUtil.getApplicationContext().getBean(HistoryService.class);
		List<HistoricTaskInstance> hisTasks= historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).taskDefinitionKey(taskDefKey).orderByTaskCreateTime().desc().list();
		return hisTasks.get(0).getAssignee();
	}

}
