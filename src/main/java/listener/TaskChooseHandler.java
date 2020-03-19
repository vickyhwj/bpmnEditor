package listener;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;

import com.activiti6.entity.NextNode;

public interface TaskChooseHandler {
	NextNode getNextNode(SequenceFlow inSeqFlow,UserTask userTask,NextNode nextNode,String[] param);

}
