package com.springcamel.demo.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class RestRouterSender extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		restConfiguration().host("localhost").port(8082);
		
		from("timer:activemq-timer?period=1000000").
		log("${body}").
		to("rest:post:/twilio/sendSMS"); 
		

	}

}
