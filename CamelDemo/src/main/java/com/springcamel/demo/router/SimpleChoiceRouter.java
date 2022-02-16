package com.springcamel.demo.router;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class SimpleChoiceRouter extends RouteBuilder {
	
	@Autowired
	DeciderBean deciderBean;

	@Override
	public void configure() throws Exception {
		
		from("file:C:\\Users\\nagdonep\\Apps\\Docs\\Camel\\Input")
		.routeId("files-router")
		.transform().body(String.class)
		.choice()
		//below snipper also called content based routing pattern
		.when(simple("${file:ext} starts with 'JSON'"))
		.log("JSON File")
//		.when(simple("${body} contains 'Vam'"))
		.when(method(deciderBean))
		.log("File with Vamsi Data..")
		.otherwise()
		.log("Not an usefull file..")
		.end()
//		.log("${messageHistory} ${file:absolute.path}")
		.to("direct:files-log-router")
		.to("file:C:\\Users\\nagdonep\\Apps\\Docs\\Camel\\Output");
		
		//Creating reusable route for logging of the files related in this case
		
		from("direct:files-log-router")
		.log("${messageHistory} ${file:absolute.path}")
		.log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
		.log("${file:onlyname.noext} ${file:parent} ${file:path}")
		.log("${file:absolute} ${file:modified} ${file:size} ")
		.log("${routeId} ${camelId} ${body}");
		
		
	}

}

@Component
class DeciderBean {
	Logger logger = LoggerFactory.getLogger(DeciderBean.class);
	public boolean isThisConditionMet(@Body String body,
			@Headers Map<String,String> headerProps,
			@ExchangeProperties Map<String,String> exchangeProps) {
		logger.info("Decider Bean method params info {} {} {} ", body, headerProps, exchangeProps);
		return true;
	}
}
