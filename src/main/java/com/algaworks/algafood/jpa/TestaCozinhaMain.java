package com.algaworks.algafood.jpa;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

//Classe de aplicacaço Spring sem ser voltado para WEB
public class TestaCozinhaMain {
	
	public static void main(String args[]) {
		
		//Constroi um aplicacao spring
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
		//Informa que não é uma WebApplication
				.web(WebApplicationType.NONE)	
				.run(args);
		
		CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
		
		System.out.println();
		
		List<Cozinha> cozinhas = cozinhaRepository.listar();
		
		System.out.println("Lista de Cozinhas Mockadas:");
		
		for (Cozinha cozinha : cozinhas) {
			System.out.println(cozinha.getNome());
		}
		
		//Adicionando Cozinhas no banco
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setNome("Brasileira");
		
		Cozinha cozinha2 = new Cozinha();
		cozinha2.setNome("Japonesa");
		
		//Como método "adicionar" retorna um objeto Cozinha, adicionamos ele dentro da lista já criada
		cozinhas.add(cozinhaRepository.salvar(cozinha1));
		cozinhas.add(cozinhaRepository.salvar(cozinha2));
		
		System.out.println("Lista de Cozinhas Mockadas:");
		for (Cozinha cozinha : cozinhas) {
			System.out.printf("%d - %s\n" ,cozinha.getId() , cozinha.getNome());
		}
		
		//Buscando uma Cozinha por ID		
		System.out.println("\nBuscando Cozinha por ID:");
		Cozinha cozinha = cozinhaRepository.buscarPorId(4L);
		System.out.println(cozinha.getNome());
		
		System.out.println("\nAlterando Cozinha:");
		Cozinha objCozinha = new Cozinha();
		objCozinha.setId(1L);
		objCozinha.setNome("Italiana");
		cozinhaRepository.salvar(objCozinha);
		
		cozinhaRepository.listar().forEach(x -> System.out.printf("%d - %s\n" ,x.getId() , x.getNome()));
		
		System.out.println("\nDeletando uma Cozinha:");
		cozinha = new Cozinha();
		cozinha.setId(1L);
		cozinhaRepository.deletar(cozinha);
			
		cozinhaRepository.listar().forEach(x -> System.out.printf("%d - %s\n" ,x.getId() , x.getNome()));
		
		System.out.println();
	}
}
