package com.activiti6.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
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
import org.springframework.util.StringUtils;

import com.activiti6.config.ExtAttrKeys;

import listener.ExtTaskListener;

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
//			taskEntity.setVariableLocal("isBack", "1");
			ActivitiContextHolder.getContext().getNextTaskIds().add(taskEntity.getId());
		}
		Map<String,String> extMap= BpmnUtils.getExtensionMap(userTask);
		String extLisClass=extMap.get(ExtAttrKeys.userTask_extListenClass);
		if(StringUtils.hasLength(extLisClass)){
			String clazz= extLisClass.substring(0, extLisClass.indexOf("("));
			String params=extLisClass.substring(extLisClass.indexOf("(")+1,extLisClass.indexOf(")"));
			String[] paramsArr=new String[0];
			if(StringUtils.hasLength(params)){
				paramsArr=params.split(",");
			}
			ExtTaskListener extTaskListener=null;
			try {
				 extTaskListener=(ExtTaskListener) Class.forName(clazz).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			extTaskListener.before(userTask, taskEntity, eventType, paramsArr);
			super.executeTaskListeners(userTask, taskEntity, eventType);
			extTaskListener.after(userTask, taskEntity, eventType, paramsArr);
		}
		else{
			super.executeTaskListeners(userTask, taskEntity, eventType);
		}
	}

	@Override
	protected BaseTaskListener createTaskListener(ActivitiListener activitiListener) {
		String impl=activitiListener.getImplementation();
		String type=activitiListener.getImplementationType();
		if("class".equals(type)&& StringUtils.hasLength( activitiListener.getImplementation())||activitiListener.getImplementation().contains("(")){
			String clazz= impl.substring(0, impl.indexOf("("));
			String params=impl.substring(impl.indexOf("(")+1,impl.indexOf(")"));
			String[] paramsArr=new String[0];
			if(StringUtils.hasLength(params)){
				paramsArr=params.split(",");
			}
			try {
				
				Constructor cus= Class.forName(clazz).getConstructor(java.lang.String[].class);
				return (BaseTaskListener) cus.newInstance(new Object[]{paramsArr});
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
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
