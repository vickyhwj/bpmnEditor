package listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;

import com.activiti6.entity.NextNode;
import com.activiti6.entity.User;

public class AssignBackChooseImpl extends TaskBackChooseHandlerImpl{

	@Override
	public NextNode getNextNodeNoBack(SequenceFlow inSequenceFlow, UserTask userTask, NextNode nextNode,
			String[] param) {
		List<User> users=new ArrayList<>();
		if(param!=null){
			for(String p:param){
				users.add(new User(p));
			}
		}
		
		nextNode.setTaskId(userTask.getId());
		nextNode.setTaskName(userTask.getName());
		nextNode.setAssignType(NextNode.assign_assignee);
		nextNode.setUser(users);
		return nextNode;
	}

}
