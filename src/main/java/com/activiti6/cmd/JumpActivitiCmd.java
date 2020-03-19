package com.activiti6.cmd;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.activiti6.service.ActivitiService;
import com.activiti6.util.BeanUtil;

public class JumpActivitiCmd implements Command<Object>{
	private String taskId;

    private String targetNodeId;

    public JumpActivitiCmd(String taskId, String targetNodeId) {
        this.taskId = taskId;
        this.targetNodeId = targetNodeId;
    }

    public Object execute(CommandContext commandContext) {
        //获取任务实例管理类
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        //获取当前任务实例
        TaskEntity currentTask = taskEntityManager.findById(taskId);

        //获取当前节点的执行实例
        ExecutionEntity execution = currentTask.getExecution();
        String executionId = execution.getId();

        //获取流程定义id
        String processDefinitionId = execution.getProcessDefinitionId();
        //获取目标节点
        org.activiti.bpmn.model.Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);
        FlowElement flowElement = process.getFlowElement(targetNodeId);

        //获取历史管理
        HistoryManager historyManager = commandContext.getHistoryManager();

        //通知当前活动结束(更新act_hi_actinst)
        historyManager.recordActivityEnd(execution,"jump to "+flowElement.getName());
        //通知任务节点结束(更新act_hi_taskinst)
        historyManager.recordTaskEnd(taskId,"jump to "+flowElement.getName());
        //删除正在执行的当前任务
        taskEntityManager.delete(taskId);

        //此时设置执行实例的当前活动节点为目标节点
        execution.setCurrentFlowElement(flowElement);
//        execution.setTransientVariableLocal("nextNode", "t23");
        //向operations中压入继续流程的操作类
        commandContext.getAgenda().planContinueProcessOperation(execution);

        return null;
    }
}
