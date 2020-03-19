package listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class MyTaskHandler implements ExecutionListener{

	@Override
	public void notify(DelegateExecution execution) {
		// TODO Auto-generated method stub
		System.out.println(execution);
		
	}

}
