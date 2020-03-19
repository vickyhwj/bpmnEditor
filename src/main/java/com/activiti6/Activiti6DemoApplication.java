package com.activiti6;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import org.springframework.context.ConfigurableApplicationContext;

import com.activiti6.config.CustomSequenceFlowXMLConverter;
import com.activiti6.util.BeanUtil;


@SpringBootApplication
public class Activiti6DemoApplication {

    public static void main(String[] args) {
    	ConfigurableApplicationContext context=SpringApplication.run(Activiti6DemoApplication.class, args);
    }

}
