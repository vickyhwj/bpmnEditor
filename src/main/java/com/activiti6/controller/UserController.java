package com.activiti6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 流程控制器 liuz1hize 2019年3月7日下午3:28:14
 */

@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private HistoryService historyService ;
	@Autowired
	private RuntimeService runtimeService;

	@ResponseBody
	@RequestMapping("/user/list/{roleId}")
	public List<User> list(@PathVariable("roleId") String roleId,HttpServletRequest request) {
		List<User> users = new ArrayList<UserController.User>();
		for (int i = 0; i <= 5; ++i) {
			User user = new User();
			user.setId((long) i);
			user.setEmployeeCode("code" + i);
			user.setUserName("name" + i+"roleId"+roleId);

			users.add(user);
		}
		return users;
	}

	@ResponseBody
	@RequestMapping("/role/allRole")
	public List<Role> roles() {
		List<Role> users = new ArrayList<UserController.Role>();
		for (int i = 0; i <= 5; ++i) {
			Role user = new Role();
			user.setId((long) i);
			user.setName("role" + i);
			user.setDescription("description" + i);

			users.add(user);
		}
		return users;
	}

	
	@RequestMapping("/test")
	@ResponseBody
	public String test(HttpServletRequest request,HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");
		String callback=request.getParameter("callback");
		
		Enumeration<String>  names=request.getHeaderNames();
		StringBuilder sb=new StringBuilder();
		while(names.hasMoreElements()){
			String name=names.nextElement();
			System.out.println(name+":"+request.getHeader(name));
			sb.append(name).append(',');
		}
		return  callback+"({\"age\":1})";
	}

	public class User {
		Long id;
		String employeeCode;
		String userName;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getEmployeeCode() {
			return employeeCode;
		}

		public void setEmployeeCode(String employeeCode) {
			this.employeeCode = employeeCode;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

	}

	public class Role {
		Long id;
		String name;
		String description;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}
}
