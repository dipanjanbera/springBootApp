package com.springboot.controller;

import java.util.List;

import com.springboot.aop.EnableRestCallLogs;
import com.springboot.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.springboot.property.Property;
import com.springboot.property.ComplexProperty;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/confProperty")
public class ConfigurationPropertyController implements GenericController{

	@Autowired
	private ComplexProperty complexProperty;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(path="/1/{id}", consumes = "application/json" , produces = "application/json")
	public List<Property> fetchConfigurationProperties(@PathVariable("id") String id, @RequestParam String val) {
		System.out.println("------------------- :"+id);
		System.out.println("------------------- :"+val);
		return null;
	}

	@PostMapping(path="/2", consumes = "application/json" , produces = "application/xml")
	public @ResponseBody Student saveConfig(@RequestBody Student student) throws Exception {
		System.out.println(student.toString());
		if(student.getAge().equalsIgnoreCase("20")){
			throw new Exception();
		}
		restTemplate.getMessageConverters().stream().forEach(obj->{
			HttpMessageConverter<?> httpMessageConverter = obj;
		});
		return student;
	}
}