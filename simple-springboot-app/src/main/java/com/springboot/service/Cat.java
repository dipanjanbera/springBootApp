package com.springboot.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Primary
@Service
@Qualifier("Cat")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode = ScopedProxyMode.INTERFACES)
public class Cat implements Animal, InitializingBean, DisposableBean {

	@Override
	public String characteristics() {
		// TODO Auto-generated method stub
		return "Meow";
	}



	@PostConstruct
	public void postConstruct(){
		System.out.println("#############################################################333vOn Post Construct ");
	}

	@PreDestroy
	public void PreDestroy(){
		System.out.println("On Pre Destroy ");
	}

	public void  beanInitMethod() {
		System.out.println("################# called ---------------------- beanInitMethod");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("################# called ---------------------- afterPropertiesSet");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("################# called ---------------------- destroy");
	}

	@PreDestroy
	public void destryMethod() throws Exception {
		System.out.println("################# called ---------------------- destryMethod");
	}


}
