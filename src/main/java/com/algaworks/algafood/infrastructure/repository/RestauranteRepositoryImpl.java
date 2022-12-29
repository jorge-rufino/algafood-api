package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

//Para o Spring identificar que aqui é implementação do metodo "find" que está declarado em "RestauranteRepository"
//é obrigatório que o nome da classe esteja exatamente assim
//Repare que mesmo sem implemertamos diretamente através da keyword "implements", o spring identifica a implementação

@Repository
public class RestauranteRepositoryImpl {
	
	@PersistenceContext
	private EntityManager manager;
	
	public List<Restaurante> find (String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		
		var jpql = "from Restaurante "
				+ "where nome like :nome "
				+ "and taxaFrete between :taxaInicial and :taxaFinal";
		
		return manager.createQuery(jpql, Restaurante.class)
				.setParameter("nome", "%"+nome+"%")
				.setParameter("taxaInicial", taxaFreteInicial)
				.setParameter("taxaFinal", taxaFreteFinal)
				.getResultList();
	}
}
