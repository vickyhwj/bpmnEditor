package com.activiti6.util;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.activiti6.config.CustomExclusiveGatewayXMLConverter;
import com.activiti6.config.CustomSequenceFlowXMLConverter;
 
@Component
public class SpringUtil implements ApplicationContextAware {
 
    private static ApplicationContext applicationContext;
 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	BpmnXMLConverter.addConverter(new CustomSequenceFlowXMLConverter());
    	BpmnXMLConverter.addConverter(new CustomExclusiveGatewayXMLConverter());
        if(SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
        System.out.println("---------------------------------------------------------------------");
 
        System.out.println("---------------------------------------------------------------------");
 
        System.out.println("---------------me.shijunjie.util.SpringUtil------------------------------------------------------");
 
        System.out.println("========ApplicationContext配置成功,在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象,applicationContext="+SpringUtil.applicationContext+"========");
 
        System.out.println("---------------------------------------------------------------------");
    }
 
    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
 
   
 
}