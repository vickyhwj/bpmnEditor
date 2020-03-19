package com.activiti6.util;

import java.util.Map;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.HasExecutionListeners;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.BaseTaskListener;
import org.activiti.engine.delegate.CustomPropertiesResolver;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.TransactionDependentExecutionListener;
import org.activiti.engine.delegate.TransactionDependentTaskListener;
import org.activiti.engine.impl.bpmn.listener.ListenerNotificationHelper;
import org.activiti.engine.impl.bpmn.parser.factory.ListenerFactory;
import org.activiti.engine.impl.cfg.TransactionListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class MyListenerNotificationHelper  extends ListenerNotificationHelper{

	@Override
	public void executeExecutionListeners(HasExecutionListeners elementWithExecutionListeners,
			DelegateExecution execution, String eventType) {
		// TODO Auto-generated method stub
		super.executeExecutionListeners(elementWithExecutionListeners, execution, eventType);
	}

	@Override
	protected void planTransactionDependentExecutionListener(ListenerFactory listenerFactory,
			DelegateExecution execution, TransactionDependentExecutionListener executionListener,
			ActivitiListener activitiListener) {
		// TODO Auto-generated method stub
		super.planTransactionDependentExecutionListener(listenerFactory, execution, executionListener, activitiListener);
	}

	@Override
	public void executeTaskListeners(TaskEntity taskEntity, String eventType) {
		// TODO Auto-generated method stub
		super.executeTaskListeners(taskEntity, eventType);
	}

	@Override
	public void executeTaskListeners(UserTask userTask, TaskEntity taskEntity, String eventType) {
		// TODO Auto-generated method stub
		if(TaskListener.EVENTNAME_CREATE.equals(eventType)){
			taskEntity.setVariableLocal("isBack", "1");
		}
		super.executeTaskListeners(userTask, taskEntity, eventType);
	}

	@Override
	protected BaseTaskListener createTaskListener(ActivitiListener activitiListener) {
		// TODO Auto-generated method stub
		return super.createTaskListener(activitiListener);
	}

	@Override
	protected void planTransactionDependentTaskListener(DelegateExecution execution,
			TransactionDependentTaskListener taskListener, ActivitiListener activitiListener) {
		// TODO Auto-generated method stub
		super.planTransactionDependentTaskListener(execution, taskListener, activitiListener);
	}

	@Override
	protected CustomPropertiesResolver createCustomPropertiesResolver(ActivitiListener activitiListener) {
		// TODO Auto-generated method stub
		return super.createCustomPropertiesResolver(activitiListener);
	}

	@Override
	protected Map<String, Object> invokeCustomPropertiesResolver(DelegateExecution execution,
			CustomPropertiesResolver customPropertiesResolver) {
		// TODO Auto-generated method stub
		return super.invokeCustomPropertiesResolver(execution, customPropertiesResolver);
	}

	@Override
	protected void addTransactionListener(ActivitiListener activitiListener, TransactionListener transactionListener) {
		// TODO Auto-generated method stub
		super.addTransactionListener(activitiListener, transactionListener);
	}
	
}
