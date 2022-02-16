package com.springcamel.demo.router;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class KafkaRouterSender extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("file:C:\\Users\\nagdonep\\Apps\\Docs\\Camel\\Input").
		log("${body}")
		.to("kafka:my-kafka-topic")
		; 
		

	}

}
