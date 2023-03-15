package com.algaworks.algafood.infrastructure.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.services.VendaQueryService;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {
		var builder = manager.getCriteriaBuilder();
		
//		Tipo de objeto que a consular irá retornar. 
		var query = builder.createQuery(VendaDiaria.class);
		
		var root = query.from(Pedido.class);
		
//		Vamos implementar este select agora: 
		
//		select date(P.data_criacao) as data_criacao, count(P.id) as total_vendas, sum(P.valor_total) as total_faturado
//		from pedido P
//		group by date(P.data_criacao)
		
//		Precisamos implementar uma função para truncar a data simulando o mesmo que o "date(P.dataCriacao)" está fazendo, 
//		que é truncar a data (2023-01-15).
//		Entao passamos o nome da funçao do mysql("date"), o tipo que o spring deve converter(Date), e o argumento("data_criacao)
				
		var functionDateDataCriacao = builder.function("date", Date.class, root.get("dataCriacao"));
		
//		Cria o "select" que será usado e indicar que será chamado construtor de "VendaDiaria.class" para cada linha retornada 
		
		var selection = builder.construct(VendaDiaria.class, 
				functionDateDataCriacao,				//Referente ao "date(P.data_criacao)
				builder.count(root.get("id")),			//Referente ao count(P.id)
				builder.sum(root.get("valorTotal")));	//Refeneten ao sum(P.valor_total)
		
		query.select(selection);
		query.groupBy(functionDateDataCriacao);
		
		return manager.createQuery(query).getResultList();
	}

}
