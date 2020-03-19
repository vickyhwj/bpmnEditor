package listener;

import java.util.Arrays;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;

import com.activiti6.entity.NextNode;
import com.activiti6.entity.User;

public class TaskChooseHandlerImpl implements TaskChooseHandler{

	@Override
	public NextNode getNextNode(SequenceFlow inSequenceFlow,UserTask userTask, NextNode nextNode,String[] param) {
		nextNode.setAssignType(NextNode.assign_assignee);
		nextNode.setTaskId(userTask.getId());
		nextNode.setTaskName(userTask.getName());
		nextNode.setUser(Arrays.asList(new User("a1"),new User("a2")));
		return nextNode;
	}

}
