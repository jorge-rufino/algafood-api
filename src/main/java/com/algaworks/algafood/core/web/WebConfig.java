package com.algaworks.algafood.core.web;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

//	Habilita o CORS para toda a API
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods("*");
//		.maxAge(10);		//Configura cache de 10segundos para toda API
	}
		
//	Metodo para gerar o hash e o cabeçalho do Etag automaticamente. Basta definirmos este método como um Bean e pronto.
	@Bean	
	public Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
	
//	Define qual o MediaType padrão deve ser utilizado caso não seja passado na requisição.
//	Por padrão, o MediaType da versão V1 está sendo utilizado porém é bom especificar mesmo assim.
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {	
		configurer.defaultContentType(AlgaMediaTypes.V1_APPLICATION_JSON);
	}
}
