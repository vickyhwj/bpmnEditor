package com.activiti6.controller.editor;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

/**
 * 获取编辑器组件及配置项信息 liu1zhize 2019年3月7日下午3:33:28
 */
@RestController
@RequestMapping("service")
public class StencilsetRestResource {

	/**
	 * 获取流程json文件
	 * 
	 * @return
	 */
	@RequestMapping(value = "/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getStencilset() {
		InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
		try {
			String jsonStr = IOUtils.toString(stencilsetStream, "utf-8");
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONArray stencils = jsonObject.getJSONArray("stencils");

			removeById("StartTimerEvent", stencils);
			removeById("StartSignalEvent", stencils);
		
			
			removeByGroup("结构列表", stencils);
			removeByGroup("中间抛出事件", stencils);
			removeByGroup("中间捕获事件列表", stencils);
			removeByGroup("边界事件", stencils);
			removeByGroup("泳道列表", stencils);

			
			return jsonObject.toString();
		} catch (Exception e) {
			throw new ActivitiException("Error while loading stencil set", e);
		}
	}

	public void removeById(String id, JSONArray jsonArray) {
		for (int i = jsonArray.length()-1; i >=0; --i) {
			if (id.equals(jsonArray.getJSONObject(i).get("id"))) {
				jsonArray.remove(i);
				break;
			}
		}

	}
	
	public void removeByGroup(String groups, JSONArray jsonArray) {
		for (int i = jsonArray.length()-1; i >=0; --i) {
			if (groups.equals(jsonArray.getJSONObject(i).getJSONArray("groups").get(0))) {
				jsonArray.remove(i);
			}
		}

	}
}
