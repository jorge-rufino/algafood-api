package com.algaworks.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

public class TestaRestauranteMain {

	public static void main(String[] args) {
		
		//Constroi um aplicacao spring
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
		//Informa que não é uma WebApplication
				.web(WebApplicationType.NONE)	
				.run(args);
		
		RestauranteRepository restauranteRepository = applicationContext.getBean(RestauranteRepository.class);
		
		System.out.println("\nListando todos os Restaurantes:");
		restauranteRepository.listar().stream().forEach(x -> System.out.println(x));		
		
		System.out.println();
	}

}
