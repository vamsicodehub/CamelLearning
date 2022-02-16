package com.springcamel.demo.router;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMQRouterRx extends RouteBuilder {

	@Autowired
	EmpProcessorBean empProcessorBean;
	
	@Autowired
	EmpTransformBean empTransformBean;

	@Override
	public void configure() throws Exception {

		from("activemq:my-activemq-queue")
//		.to("file:C:\\Users\\nagdonep\\Apps\\Docs\\Camel\\Output")
		.log("${body}").unmarshal()
		.json(JsonLibrary.Jackson, Employee.class)// Have to add a jackson (camel-jackson-starter) library to pom.xml
		.bean(() -> empProcessorBean)
		.bean(empTransformBean)
		.to("log:received-message-from--activemq-queue");
	}

}

@Component
class EmpProcessorBean {

	Logger logger = LoggerFactory.getLogger(EmpProcessorBean.class);

	public void processBean(Employee employee) {
		logger.info("Processing Bean with name {} ", employee.getName());
	}

}

@Component
class EmpTransformBean {

	Logger logger = LoggerFactory.getLogger(EmpTransformBean.class);

	public Employee transformBean(Employee employee) {
		logger.info("EmpTransformBean Bean with name {} ", employee.getName());
		employee.setSal(employee.getSal() * 12);
		return employee;
	}

}
