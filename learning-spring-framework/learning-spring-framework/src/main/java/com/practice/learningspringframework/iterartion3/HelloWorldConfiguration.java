package com.practice.learningspringframework.iterartion3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

record Person(String name,int age) {};

record Address(String city, int streetNo) {};

@Configuration
public class HelloWorldConfiguration {
	@Bean
	public String name() {
		return "Ranga";
	}
	
	@Bean
	public int age() {
		return 15;
	}
	
	@Bean
	public Person person() {
		var person = new Person("Ravi",20);
		return person;
	}
	
	@Bean(name="yourAddress")
	public Address address() {
		var address = new Address("Nahan",11);
		return address;
	}
}
