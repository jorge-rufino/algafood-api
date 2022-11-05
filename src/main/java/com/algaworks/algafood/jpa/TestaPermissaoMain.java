package com.algaworks.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

public class TestaPermissaoMain {

	public static void main(String[] args) {
		
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
		//Informa que não é uma WebApplication
				.web(WebApplicationType.NONE)	
				.run(args);
		
		PermissaoRepository repository = applicationContext.getBean(PermissaoRepository.class);
		
		System.out.println("\nLista de Permissões:");
		repository.listar().forEach(x -> System.out.println(x));
		
		System.out.println("\nBuscando pelo ID: Id = 2");
		System.out.println(repository.buscar(2L));
		
		System.out.println("\nSalvando nova Permissao:");
		Permissao permissao = new Permissao();
		permissao.setNome("Administrador");
		permissao.setDescricao("Tem todas as permissões");
		System.out.println(repository.salvar(permissao));
		
		System.out.println("\nDeletando por ID: Id = 2");
		repository.deletar(repository.buscar(2L));
		
		System.out.println("\nLista de Permissões:");
		repository.listar().forEach(x -> System.out.println(x));
		
		System.out.println();
	}

}
