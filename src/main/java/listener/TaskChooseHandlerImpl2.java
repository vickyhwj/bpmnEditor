package listener;

import java.util.Arrays;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.activiti6.config.ExtAttrKeys;
import com.activiti6.entity.NextNode;
import com.activiti6.entity.User;

public class TaskChooseHandlerImpl2 implements TaskChooseHandler,ExecutionListener{

	@Override
	public NextNode getNextNode(SequenceFlow inSeqlFlow,UserTask userTask, NextNode nextNode,String[] param) {
		nextNode.setAssignType(NextNode.assign_candidateUser);
		nextNode.setTaskId(userTask.getId());
		nextNode.setTaskName(userTask.getName());
		nextNode.setUser(Arrays.asList(new User("a3"),new User("a4")));
		return nextNode;
	}

	@Override
	public void notify(DelegateExecution execution) {
		// TODO Auto-generated method stub
		execution.setTransientVariable(ExtAttrKeys.nextNode, "taska");
	}

}
