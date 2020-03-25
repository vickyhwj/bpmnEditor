package listener;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public interface ExtTaskListener {
	void before(UserTask userTask, TaskEntity taskEntity, String eventType,String[] param);
	
	void after(UserTask userTask, TaskEntity taskEntity, String eventType,String[] param);

}
