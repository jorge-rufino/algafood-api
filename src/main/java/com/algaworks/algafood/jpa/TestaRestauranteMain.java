package com.algaworks.algafood.jpa;

import java.math.BigDecimal;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Restaurante;
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
		
		System.out.println("\nAdicionando um novo restaurante:");
		Restaurante restaurante1 = new Restaurante();
		restaurante1.setNome("Fogao a lenha");
		restaurante1.setTaxaFrete(new BigDecimal("3.0"));		
		
		Restaurante restaurante2 = new Restaurante();
		restaurante2.setNome("Lokko Pizza");
		restaurante2.setTaxaFrete(new BigDecimal("2.0"));
		
		restauranteRepository.salvar(restaurante1);
		restauranteRepository.salvar(restaurante2);
		
		System.out.println("\nListando todos os Restaurantes:");
		restauranteRepository.listar().stream().forEach(x -> System.out.println(x));
		
		System.out.println("\nBuscando um Restaurante por ID: (Id = 2)");		
		System.out.println(restauranteRepository.buscarPorId(2L));
		
		System.out.println("\nAlterando um Restaurante por ID: (Id = 3)");
		Restaurante restaurante = restauranteRepository.buscarPorId(3L);
		restaurante.setNome("Colher de Pau");
		restaurante.setTaxaFrete(new BigDecimal("7.0"));
		restauranteRepository.salvar(restaurante);
		
		System.out.println("\nListando todos os Restaurantes:");
		restauranteRepository.listar().stream().forEach(x -> System.out.println(x));
		
		System.out.println("\nDeletando um Restaurante:Id = 2");
		restaurante.setId(2L);
		restauranteRepository.deletar(restaurante);
		
		System.out.println("\nListando todos os Restaurantes:");
		restauranteRepository.listar().stream().forEach(x -> System.out.println(x));
		
		System.out.println();
	}

}
