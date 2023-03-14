package com.algaworks.algafood.infrastructure.repository.spec;

import java.util.ArrayList;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.filter.PedidoFilter;

public class PedidosSpecs {
	//Specification espera como retorno um Predicate. 
	public static Specification<Pedido> usandoFiltro(PedidoFilter filtro){
		return (root, query, builder) -> {
			
//			Faz os "fetch" para evitar o problema do N+1
			
//	Na paginacao, ele faz um select em Pedido e um "select count" que não pode ter o "fetch", para evitar este problema
//	verificamos se o select é em Pedido e se for ai sim ele faz os "fetchs". 
			
			if(Pedido.class.equals(query.getResultType())) {
				root.fetch("restaurante").fetch("cozinha");
				root.fetch("cliente");
			}
			
			var predicates = new ArrayList<Predicate>();
			
//		Perceba que os valores do "root.get()" tem que receber os mesmos nomes das variaves da classe Pedido
			
			if (filtro.getClienteId() != null) {
				predicates.add(builder.equal(root.get("cliente"), 
											filtro.getClienteId()));
			}
			
			if (filtro.getRestauranteId() != null) {
				predicates.add(builder.equal(root.get("restaurante"), 
											filtro.getRestauranteId()));
			}
			
			if (filtro.getDataCriacaoInicio() != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), 
															filtro.getDataCriacaoInicio()));
			}
			
			if (filtro.getDataCriacaoFim() != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), 
														filtro.getDataCriacaoFim()));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
}
