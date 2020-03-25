package listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;

import com.activiti6.entity.NextNode;
import com.activiti6.entity.User;

public class TaskChooseHandlerImpl implements TaskChooseHandler{

	@Override
	public NextNode getNextNode(SequenceFlow inSequenceFlow,UserTask userTask, NextNode nextNode,String[] param) {
		
		nextNode.setAssignType(param[0]);
		nextNode.setTaskId(userTask.getId());
		nextNode.setTaskName(userTask.getName());
		
		List<User> users=new ArrayList<User>();
		for(int i=1;i<param.length;++i){
			users.add(new User(param[i]));
		}
		nextNode.setUser(users);
		return nextNode;
	}

}
