package listener;

import java.util.List;

import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.SequenceFlow;

import com.activiti6.entity.NextNode;
import com.activiti6.service.ActivitiService;
import com.activiti6.util.SpringUtil;

public class DefaultExclusiveGatewayChooseHandler implements ExclusiveGatewayChooseHandler{

	@Override
	public NextNode getNextNode(SequenceFlow inSeqFlow,ExclusiveGateway gateway, NextNode nextNode,String[] param) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ActivitiService activitiService= SpringUtil.getApplicationContext().getBean(ActivitiService.class);
		List<NextNode> nextNodes= activitiService.getNextNodesBySeqs(gateway.getOutgoingFlows());
		nextNode.setAssignType(NextNode.assign_exclusiveGateWay);
		nextNode.setNextNodes(nextNodes);
		return nextNode;
	}

}
