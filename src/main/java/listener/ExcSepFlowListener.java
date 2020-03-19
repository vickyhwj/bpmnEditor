package listener;

import java.util.Map;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.activiti6.config.ExtAttrKeys;
import com.activiti6.entity.NextNode;
import com.activiti6.service.ActivitiService;
import com.activiti6.util.BpmnUtils;
import com.activiti6.util.SpringUtil;

public class ExcSepFlowListener implements ExecutionListener{

	@Override
	public void notify(DelegateExecution execution) {
		// TODO Auto-generated method stub
		execution.setTransientVariable(ExtAttrKeys.nextNode, "taska");
		
	}

}
