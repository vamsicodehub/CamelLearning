package com.springcamel.demo.router.patterns;

import org.apache.camel.builder.RouteBuilder;

public class EIPPatternRx extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("activemq:split-csv-file")
		.to("log:csv-file");
	}

}
