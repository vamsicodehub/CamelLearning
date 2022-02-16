package com.springcamel.demo.router.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springcamel.demo.router.Employee;

@Component
public class EIPPatternSender extends RouteBuilder {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	SplitterComponent splitterComponent;

	@Autowired
	private DynamicRouter dynamicRouter;

	@Override
	public void configure() throws Exception {
		//pipeline - default pattern
		//content based routing pattern implemented in Simple Choice Route (when)
		
		//multicast pattern below is th eg.
		 from("timer:multipattern?period=10000000000")
		.routeId("EIPPatern")
		.multicast()
		.to("log:message1", "log:message2",  "log:message3", "log:message4");
		 
		 //Split Pattern
		 from("file:C:\\Users\\nagdonep\\Apps\\Docs\\Camel\\Input\\csv")
		 .routeId("CSV-Split-Route")
		 .unmarshal().csv()
//		 .split(body(), ",")
		 .split(method(splitterComponent))
//		 .to("activemq:split-csv-file")
		 .to("log:csv-file");
		 
		 logger.info("About to start Aggregate Pattern");
		 
		 //Aggregate Pattern
		 from("file:C:\\Users\\nagdonep\\Apps\\Docs\\Camel\\Input\\agg-json")
		 .routeId("agg-route")
		 .unmarshal().json(JsonLibrary.Jackson, Employee.class)
		 .aggregate(simple("${body.name}"), (oldExchange, newExchange) -> {
			 Object newBody = newExchange.getIn().getBody();
				List<Object> aggList = null;
				if(oldExchange == null) {
					aggList = new ArrayList<>();
					aggList.add(newBody);
					newExchange.getIn().setBody(aggList);
					return newExchange;
				}else {
					aggList = (ArrayList<Object>) oldExchange.getIn().getBody();
					aggList.add(newBody);
					return oldExchange;
				}
		 	}
		 )
		 .completionSize(3)
//		 .completionTimeout(HIGHEST)
		 .to("log:agg-json");
		 
		 //RoutingSlip Pattern
		 
		 String routingSlip = "direct:endpoint1, direct:endpoint2, direct:endpoint3, direct:endpoint4";
		 
		 from("timer:routingSlip?period=1000000000")
		 .transform().constant("Routing Slip Constant message....")
		 .routingSlip(simple(routingSlip))
		 ;
		 
		 from("direct:endpoint1")
		 .to("log:directendpoint1");
		 
		 from("direct:endpoint2")
		 .to("log:directendpoint2");
		 
		 getContext().setTracing(true);
//		 errorHandler(deadLetterChannel("activemq:dead-letter-queue")); //can be used to store failure tasks to the queue
		 
		 from("direct:endpoint3")
		 .to("log:directendpoint3");
		 
		 from("direct:endpoint4")
//		 .wireTap("log:wire-tap")// to redirect to other endpoint for some task
		 .to("log:directendpoint4");
		 
		 //Dynamic Routing
		 from("timer:dynamicRouter?period={{dynamic-routing-time-period}}") //can configure things like endpoints an dothers in aplication.proos and use it like {{}}
		 .transform().constant("Routing Slip Constant message....")
		 .dynamicRouter(method(dynamicRouter));
		 
		 
	}

}


@Component
class SplitterComponent {
	
	public List<String> splitCsv(String body) {
		return List.of(body.split(","));
	}
}

@Component
class DynamicRouter {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	int varCount;
	
	public String decideRoute(@Body String body,
			@Headers Map<String,String> headerProps,
			@ExchangeProperties Map<String,String> exchangeProps) {
		
		logger.info("Decider Bean method params info {} {} {} ", body, headerProps, exchangeProps);
		varCount++;
		if(varCount % 3 == 0) 
			return "direct:endpoint1,direct:endpoint3";
		if(varCount % 3 == 1) 
			return "direct:endpoint2,direct:endpoint4";
		
		return "";
	}
	
}


/*
 * class ArraylistAggregationStrategy implements AggregationStrategy {
 * 
 * Logger logger = LoggerFactory.getLogger(getClass());
 * 
 * @Override public Exchange aggregate(Exchange oldExchange, Exchange
 * newExchange) { logger.info("Inside aggregate method...."); Object newBody =
 * newExchange.getIn().getBody(); List<Object> aggList = null; if(oldExchange ==
 * null) { aggList = new ArrayList<>(); aggList.add(newBody);
 * newExchange.getIn().setBody(aggList); return newExchange; }else { aggList =
 * oldExchange.getIn().getBody(ArrayList.class); aggList.add(newBody); return
 * oldExchange; } }
 * 
 * }
 */