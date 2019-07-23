package com.activiti6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class Activiti6DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Activiti6DemoApplication.class, args);
    }

}
