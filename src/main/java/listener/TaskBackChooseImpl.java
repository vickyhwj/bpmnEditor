package listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;

import com.activiti6.entity.NextNode;
import com.activiti6.entity.User;

public class TaskBackChooseImpl extends TaskBackChooseHandlerImpl{

	@Override
	public NextNode getNextNodeNoBack(SequenceFlow inSequenceFlow, UserTask userTask, NextNode nextNode,
			String[] param) {
		return new TaskChooseHandlerImpl().getNextNode(inSequenceFlow, userTask, nextNode, param);
	}

}
