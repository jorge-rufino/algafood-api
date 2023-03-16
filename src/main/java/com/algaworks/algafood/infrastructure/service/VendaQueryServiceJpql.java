package com.algaworks.algafood.infrastructure.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.services.VendaQueryService;

//Implementação da classe "VendaQueryService" utilizando JPQL
//Para utiliza-la basta descomentar o "@Repository" daqui e comentar o da "VendaQueryService"

//@Repository
public class VendaQueryServiceJpql implements VendaQueryService{

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffSet) {
		StringBuilder jpql = new StringBuilder(
				"SELECT new com.algaworks.algafood.domain.model.dto.VendaDiaria(" +
				"FUNCTION('date', p.dataCriacao), COUNT(p.id), SUM(p.valorTotal)) " +
				"FROM Pedido p ");
		
		String where = " WHERE p.status IN ('CONFIRMADO','ENTREGUE') ";
		boolean setDataCriacaoInicio = false;
		boolean setDataCriacaoFim = false;
		boolean setRestauranteId = false;

		if (filtro.getDataCriacaoInicio() != null) {
			where += " AND p.dataCriacao >= :dataCriacaoInicio ";
			setDataCriacaoInicio = true;
		}

		if (filtro.getDataCriacaoFim() != null) {
			where += " AND p.dataCriacao <= :dataCriacaoFim ";
			setDataCriacaoFim = true;
		}

		if (filtro.getRestauranteId() != null) {
			where += " AND  p.restaurante.id = :restauranteId ";
			setRestauranteId = true;
		}
		
		jpql.append(where);
		
		jpql.append("GROUP BY FUNCTION('date', p.dataCriacao)");

		TypedQuery<VendaDiaria> query = manager.createQuery(jpql.toString(), VendaDiaria.class);

		if (setDataCriacaoInicio) {
			query.setParameter("dataCriacaoInicio", filtro.getDataCriacaoInicio());
		}

		if (setDataCriacaoFim) {
			query.setParameter("dataCriacaoFim", filtro.getDataCriacaoFim());
		}

		if (setRestauranteId) {
			query.setParameter("restauranteId", filtro.getRestauranteId());
		}
		
		return query.getResultList();
	}

}
