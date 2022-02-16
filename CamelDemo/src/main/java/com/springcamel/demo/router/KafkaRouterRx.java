package com.springcamel.demo.router;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class KafkaRouterRx extends RouteBuilder {

	@Autowired
	EmpProcessorBean empProcessorBean;
	
	@Autowired
	EmpTransformBean empTransformBean;

	@Override
	public void configure() throws Exception {

		from("kafka:my-kafka-topic")
		.to("file:C:\\Users\\nagdonep\\Apps\\Docs\\Camel\\Output")
		.log("${body}").unmarshal()
		.json(JsonLibrary.Jackson, Employee.class)// Have to add a jackson (camel-jackson-starter) library to pom.xml
		.bean(() -> empProcessorBean)
		.bean(empTransformBean)
		.to("log:received-message-from-kafka-topic");
	}

}
