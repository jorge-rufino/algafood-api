package com.algaworks.algafood.infrastructure.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.services.VendaQueryService;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class);
		var root = query.from(Pedido.class);
		var functionDateDataCriacao = builder.function("date", Date.class, root.get("dataCriacao"));
		
		var selection = builder.construct(VendaDiaria.class, 
				functionDateDataCriacao,				//Referente ao "date(P.data_criacao)
				builder.count(root.get("id")),			//Referente ao count(P.id)
				builder.sum(root.get("valorTotal")));	//Referente ao sum(P.valor_total)

		var predicates = new ArrayList<Predicate>();
		
//		Filtros
		if(filtro.getRestauranteId() != null) {
			predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
		}
		
		if(filtro.getDataCriacaoInicio() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
		}
		
		if(filtro.getDataCriacaoFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
		}
//		Traz somente se status for "criado" ou "entregue"
		predicates.add(root.get("status").in(StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));
		
		query.where(predicates.toArray(new Predicate[0]));
		query.select(selection);
		query.groupBy(functionDateDataCriacao);
		
		return manager.createQuery(query).getResultList();
	}

}