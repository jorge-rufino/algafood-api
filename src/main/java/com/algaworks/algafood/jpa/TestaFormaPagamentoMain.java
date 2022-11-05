package com.algaworks.algafood.jpa;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

public class TestaFormaPagamentoMain {

	public static void main(String[] args) {
		//Constroi um aplicacao spring
		ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
		//Informa que não é uma WebApplication
				.web(WebApplicationType.NONE)	
				.run(args);
		
		FormaPagamentoRepository repository = applicationContext.getBean(FormaPagamentoRepository.class);
		
		System.out.println("\nFormas de Pagamento:");
		repository.listar().forEach(x -> System.out.println(x));
		
		System.out.println("\nBuscando por ID: Id = 2");
		System.out.println(repository.buscarPorId(2L));
		
		System.out.println("\nAdicionando nova Forma de Pagamento:");
		FormaPagamento formaPagamento = new FormaPagamento();
		formaPagamento.setDescricao("Pix");
		System.out.println(repository.salvar(formaPagamento));
		
		System.out.println("\nDeletando por ID: Id = 2");
		repository.deletar(repository.buscarPorId(2L));
		
		System.out.println("\nFormas de Pagamento:");
		repository.listar().forEach(x -> System.out.println(x));
		
		System.out.println();

	}

}
