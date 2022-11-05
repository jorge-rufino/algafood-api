package com.algaworks.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

public class TestaEstadoMain {

	public static void main(String[] args) {
		
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
		//Informa que não é uma WebApplication
				.web(WebApplicationType.NONE)	
				.run(args);
		
		EstadoRepository repository = applicationContext.getBean(EstadoRepository.class);
		
		System.out.println("\nLista de Estados:");
		repository.listar().forEach(x -> System.out.println(x));
		
		System.out.println("\nBuscando pelo ID: Id = 2");
		System.out.println(repository.buscarPorId(2L));
		
		System.out.println("\nSalvando nova Estado:");
		Estado estado = new Estado();
		estado.setNome("Pará");
		System.out.println(repository.salvar(estado));
		
		System.out.println("\nDeletando por ID: Id = 4");
		repository.deletar(repository.buscarPorId(4L));
		
		System.out.println("\nLista de Estados:");
		repository.listar().forEach(x -> System.out.println(x));
		
		System.out.println();
	}

}
