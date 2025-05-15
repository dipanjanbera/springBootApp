package com.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAspectJAutoProxy
@Configuration
@ComponentScan(basePackages = {"com.springboot.controller","com.springboot.property"})
public class StudentConfig implements WebMvcConfigurer {

	/*@Autowired
	private RequestHeaderInterceptor requestHeaderInterceptor;*/

	/*@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestHeaderInterceptor);
	}
*/
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(new ObjectMapper());
		restTemplate.getMessageConverters().add(converter);
		return restTemplate;
	}

	@Bean()
	String userDetails(){
		return new String("ca");
	}


}
