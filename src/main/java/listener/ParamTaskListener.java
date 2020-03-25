package listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public abstract class ParamTaskListener implements TaskListener{

	String[] param;

	public ParamTaskListener(String[] param) {
		super();
		this.param = param;
	}
	
	

}
