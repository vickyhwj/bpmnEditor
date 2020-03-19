package listener;

import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;

import com.activiti6.entity.NextNode;

public interface ExclusiveGatewayChooseHandler {
	NextNode  getNextNode(SequenceFlow inSeqFlow,ExclusiveGateway gateway,NextNode nextNode,String[] param) throws ClassNotFoundException, InstantiationException, IllegalAccessException;

}
