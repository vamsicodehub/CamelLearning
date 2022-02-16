package com.springcamel.demo.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMQRouterSender extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		//Basic Camel test code
		
	
		from("timer:activemq-timer?period=10000000")
		.transform().constant("Test Message....")
		.log("${body}")
		.to("activemq:my-activemq-queue"); 
		
		/*
		 * from("file:C:\\Users\\nagdonep\\Apps\\Docs\\Camel\\Input"). log("${body}").
		 * to("activemq:my-activemq-queue");
		 */
		

	}

}
