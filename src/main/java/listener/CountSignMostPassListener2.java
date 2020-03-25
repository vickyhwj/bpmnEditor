package listener;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountSignMostPassListener2 extends ParamTaskListener {

	public CountSignMostPassListener2(String[] param) {
		super(param);
		// TODO Auto-generated constructor stub
	}

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Integer getOrSet(DelegateTask taskEntity, String name) {
		Integer num = taskEntity.getVariable(name, Integer.class);
		if (num == null) {
			num = 0;
			taskEntity.setVariable(name, 0);
		}
		return num;
	}
	int getVar(DelegateTask delegateTask,String name){
		Integer num= delegateTask.getVariable(name, Integer.class);
		if(num==null){
			num=0;
		}
		return num;
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		String varloc = (String) delegateTask.getVariableLocal(param[0]);
		Integer num = getOrSet(delegateTask, varloc);

		delegateTask.setVariable(varloc, num + 1);
		logger.debug("set num=" + (num + 1));
		String varName = param[1];

		Integer nrOfActiveInstances = (Integer) delegateTask.getVariable("nrOfActiveInstances");
		if (nrOfActiveInstances == 1) {
			String maxvar = param[2];
			for (int i = 2; i < param.length; ++i) {
				if (getVar(delegateTask, param[i]) > getVar( delegateTask, maxvar) ) {
					maxvar = param[i];
				}
			}
			delegateTask.setVariable(varName, maxvar);
		}

	}

}
