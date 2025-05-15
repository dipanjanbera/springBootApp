package com.springboot.controller;

import com.springboot.aop.LogRestCallExecutionTime;
import com.springboot.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import com.springboot.service.Animal;

import java.util.Arrays;
import java.util.function.Function;

@RestController
@RequestMapping("/autowire")
@LogRestCallExecutionTime
public class AutowiredController {

	@Autowired
	@Qualifier("Dog")
	@Lazy
	private Animal animal;


	@Autowired
	ApplicationContext ctx;
	//Setter Injection


	//Construction Injection
    /*@Autowired
	AutowiredController(@Qualifier("dog") Animal animal) {
		this.animal = animal;
	}*/

	//Setter Injection is non-mandatory injection, server will start and work
    /*@Autowired(required = false)
	public void setAnimal(@Qualifier("dog") Animal animal) {
		this.animal = animal;
	}*/

	//Construction Injection is mandatory Injection and this will not work and server will not start
    /*@Autowired(required = false)
		AutowiredController(@Qualifier("dog") Animal animal) {
			this.animal = animal;
		}*/

	//Using Qualifier
    /*@Qualifier("cat")
	@Autowired
	private Animal animal;*/

	@GetMapping(path = "/ch/{id}")
	public @ResponseBody String fetchDogCharacteristics(@PathVariable("id") String id) {

		return animal.characteristics();
	}

	@GetMapping(path = "/info/{id}")
	public @ResponseBody String fetchInfo(@PathVariable("id") String id) {

		return ctxController.apply(ctx);
	}

	@PostMapping(path = "/student", produces = "application/json" , consumes = "application/json")
	public @ResponseBody Student fetchInfo(@RequestBody Student student) {
		return student;
	}

	public void testMethod(){
		System.out.println("--------------------------------------------");
	}

	Function<ApplicationContext, String> ctxController = ctx -> {
		StringBuilder sb = new StringBuilder("<html><body>");
		sb.append("Hello there dear developer,here are the beans you were looking for: </br>");
		//method that returns all the bean names in the context of the application
		Arrays.stream(ctx.getBeanDefinitionNames()).sorted().forEach(
				beanName -> sb.append("\n").append(beanName)
		);
		sb.append("</body></htm>");
		return sb.toString();
	};

}
