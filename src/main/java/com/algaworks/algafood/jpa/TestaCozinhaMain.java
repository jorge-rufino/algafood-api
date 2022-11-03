package com.algaworks.algafood.jpa;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;

//Classe de aplicacaço Spring sem ser voltado para WEB
public class TestaCozinhaMain {
	
	public static void main(String args[]) {
		
		//Constroi um aplicacao spring
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
		//Informa que não é uma WebApplication
				.web(WebApplicationType.NONE)	
				.run(args);
		
		CadastroCozinha cadastroCozinha = applicationContext.getBean(CadastroCozinha.class);
		
		System.out.println();
		
		List<Cozinha> cozinhas = cadastroCozinha.listar();
		
		System.out.println("Lista de Cozinhas Mockadas:");
		
		for (Cozinha cozinha : cozinhas) {
			System.out.println(cozinha.getNome());
		}
		
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Brasileira");
		
		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Japonesa");
		
		//Como método "adicionar" retorna um objeto Cozinha, adicionamos ele dentro da lista já criada
		cozinhas.add(cadastroCozinha.adicionar(cozinha1));
		cozinhas.add(cadastroCozinha.adicionar(cozinha2));
		
		for (Cozinha cozinha : cozinhas) {
			System.out.printf("%d - %s\n" ,cozinha.getId() , cozinha.getNome());
		}
		
		System.out.println();
	}
}
