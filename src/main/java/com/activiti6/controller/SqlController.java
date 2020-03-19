package com.activiti6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SqlController {
	@Autowired
	JdbcTemplate jdbcTemplate;
	@ResponseBody
	@RequestMapping("/sql")
	public Object test(String id){
//		return "2ww";
		if(id.contains(" ")) return "xxxx";
		return jdbcTemplate.queryForList("select * from act_ru_task where id_ = '"+id+"'");
	}
}
