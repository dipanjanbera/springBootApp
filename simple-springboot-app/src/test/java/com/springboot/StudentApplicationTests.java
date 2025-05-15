package com.springboot;

import com.springboot.config.OneBeanConfig;
import com.springboot.config.StudentConfig;
import com.springboot.service.SimpleBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentApplicationTests {

	@Test
	public void testOneBeanConfiguration() {
		ApplicationContext ctx =
				new AnnotationConfigApplicationContext(OneBeanConfig.class);
		System.out.println(ctx);
		String[] strings = ctx.getBeanDefinitionNames();
		System.out.println(strings);
		SimpleBean simpleBeanOne = ctx.getBean(SimpleBean.class);
		SimpleBean simpleBeanTwo = ctx.getBean(SimpleBean.class);
		if(simpleBeanOne==simpleBeanTwo){
			System.out.println("TRUE @@@@@@@@@@@@@@@@@@@@");
		}else{
			System.out.println("FALSE @@@@@@@@@@@@@@@@@@@@");
		}
		//Assertions.assertEquals(simpleBeanTwo, simpleBeanOne);
	}

}
