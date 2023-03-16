package com.algaworks.algafood.infrastructure.service.query;

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
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffSet) {
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class);
		var root = query.from(Pedido.class);
		
//		Como estamos salvando as datas em UTC, elas estão com 3 horas a mais portando pode acontecer de um pedido ser criado
//		em um dia mas no banco está em outro, como por exemplo o pedido de ID 4 que está com a seguinte data "2023-02-02 02:00:04", 
//		em UTC ele foi criado no dia 02/02 porém ao convertermos para nosso horario ele passa a ter sido criado "2023-02-01 23:00:04"
//		ou seja, 3 horas a menos.
//		
//		Precisamos modificar a query para a seguinte:
		
//			select date(convert_tz(P.data_criacao,'+00:00','-03:00')) as data_criacao, 
//			count(P.id) as total_vendas, 
//			sum(P.valor_total) as total_faturado
//			from pedido P
//			where P.status in ('CONFIRMADO', 'ENTREGUE')
//			group by date(convert_tz(P.data_criacao,'+00:00','-03:00'))
//			order by date(P.data_criacao);	
		
//		Aqui convertemos o "dataCriacao" com offset passado
		var functionConvertTzDataCriacao = builder.function(
				"convert_tz", Date.class, root.get("dataCriacao"), builder.literal("+00:00"), builder.literal(timeOffSet));
		
//		Aqui convertemos a data ja no horario convertido para o tipo "Date"
		var functionDateDataCriacao = builder.function("date", Date.class, functionConvertTzDataCriacao);
		
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
