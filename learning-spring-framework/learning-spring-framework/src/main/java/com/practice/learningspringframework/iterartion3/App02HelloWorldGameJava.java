package com.practice.learningspringframework.iterartion3;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App02HelloWorldGameJava {
    public static void main(String[] args) {
//    	launch a spring context
    	
    	try(var context = new AnnotationConfigApplicationContext(HelloWorldConfiguration.class);){
    		
        	
//        	configure the things we want the spring to configure
        	
//        	retrieving bean managed by Spring
        	System.out.println(context.getBean("name"));
        	System.out.println(context.getBean("age"));
        	System.out.println(context.getBean("person"));
        	System.out.println(context.getBean("yourAddress"));
        	System.out.println(context.getBean(Address.class));
    	}
    	
    }
}
