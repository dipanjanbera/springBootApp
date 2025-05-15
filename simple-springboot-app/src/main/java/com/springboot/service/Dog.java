package com.springboot.service;

import com.springboot.aop.EnableRestCallLogs;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Qualifier("Dog")
@Description("Salary for an employee might change,so this is a suitable example for a prototype scoped bean")
public class Dog implements Animal {


	@Override
	public String characteristics() {
		// TODO Auto-generated method stub
		return "Bark";
	}
}
