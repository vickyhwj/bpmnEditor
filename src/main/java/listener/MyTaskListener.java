package listener;

import java.util.Arrays;
import java.util.Map;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.activiti6.service.ActivitiService;
import com.activiti6.util.BeanUtil;
import com.activiti6.util.BpmnUtils;
import com.activiti6.util.SpringUtil;

public class MyTaskListener implements TaskListener{

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		ActivitiService activitiService= SpringUtil.getApplicationContext().getBean(ActivitiService.class);
		UserTask userTask= activitiService.getUserTask( delegateTask.getTaskDefinitionKey(),delegateTask.getProcessDefinitionId());
		Map<String, String> extisonMap= BpmnUtils.getExtensionMap(userTask);
		userTask.getExtensionElements().get("ext").get(0).getElementText();
		activitiService.getUserOnUserTask(userTask, null);
		delegateTask.addCandidateUsers(Arrays.asList(new String[]{"gdyxsh","gdyxlr"}));
		System.out.print(delegateTask);
		
	}

}
