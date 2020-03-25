package com.activiti6.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.activiti6.entity.TaskRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Mapper
public interface TaskRecordMapper extends BaseMapper<TaskRecord>{
	void deleteByDeploymentId(String deploymentId);
}
