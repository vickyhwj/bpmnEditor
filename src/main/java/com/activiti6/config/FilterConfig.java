package com.activiti6.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.activiti6.entity.User;
import com.activiti6.filter.ActivitiContextFilter;

@Configuration
public class FilterConfig {
	
	
	@Bean
	 public FilterRegistrationBean activitiContextFilter() {
	     FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	     registrationBean.setFilter(new ActivitiContextFilter()); // 自己的filter

	     List<String> urlPatterns = new ArrayList<>();
	     urlPatterns.add("/*");
	     registrationBean.setUrlPatterns(urlPatterns);
	     return registrationBean;
	 }
}
