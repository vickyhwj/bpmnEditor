package listener;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountSignMostPassListener implements ExtTaskListener{
	
	protected final  Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void before(UserTask userTask, TaskEntity taskEntity, String eventType, String[] param) {
		// TODO Auto-generated method stub
		if(TaskListener.EVENTNAME_COMPLETE.equals(eventType)){
			String varloc= (String) taskEntity.getVariableLocal(param[0]);
			Integer num=getOrSet(taskEntity, varloc);
			logger.debug("before sleep");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			taskEntity.setVariable(varloc, num+1);
			logger.debug("set num="+(num+1));
			String varName=param[1];
		
			Integer nrOfActiveInstances=(Integer) taskEntity.getVariable("nrOfActiveInstances");
			if(nrOfActiveInstances==1){
				String maxvar=param[2];
				for(int i=2;i<param.length;++i){
					if (taskEntity.getVariable(param[i], Integer.class)>taskEntity.getVariable(maxvar, Integer.class)) {
						maxvar=param[i];
					}
				}
				taskEntity.setVariable(varName, maxvar);
			}
		}
		
	}
	
	private Integer getOrSet(TaskEntity taskEntity,String name){
		Integer num=taskEntity.getVariable(name, Integer.class);
		if(num==null){
			num=0;
			taskEntity.setVariable(name, 0);
		}
		return num;
	}
	
	@Override
	public void after(UserTask userTask, TaskEntity taskEntity, String eventType, String[] param) {
		// TODO Auto-generated method stub
		
	}

}
