package com.algaworks.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;

public class TestaCidadeMain {

	public static void main(String[] args) {
		
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
		//Informa que não é uma WebApplication
		.web(WebApplicationType.NONE)	
		.run(args);
		
		CidadeRepository repository = applicationContext.getBean(CidadeRepository.class);
		EstadoRepository estadoReposiory = applicationContext.getBean(EstadoRepository.class);
		
		System.out.println("\nLista de Cidades:");
		repository.listar().forEach(x -> System.out.println(x));
		
		System.out.println("\nBuscando pelo ID: Id = 2");
		System.out.println(repository.buscarPorId(2L));
		
		System.out.println("\nSalvando nova Cidade:");
		Cidade cidade = new Cidade();
		cidade.setNome("Osasco");
		cidade.setEstado(estadoReposiory.buscarPorId(2L));
		System.out.println(repository.salvar(cidade));
		
		System.out.println("\nDeletando por ID: Id = 4");
		repository.deletar(repository.buscarPorId(4L));
		
		System.out.println("\nLista de Cidades:");
		repository.listar().forEach(x -> System.out.println(x));
		
		System.out.println();

	}

}
