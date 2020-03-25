package listener;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
/**
 * 一票通过
 * @author vicky
 *
 */
public class CountSignOnePassListener extends ParamTaskListener{

	

	public CountSignOnePassListener(String[] param) {
		super(param);
		// TODO Auto-generated constructor stub
	}

	

	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		String localVar= (String) delegateTask.getVariableLocal(param[0]);
		if(param[2].equals(localVar)){
			delegateTask.setVariable(param[1], localVar);
		}
		
	}

}
