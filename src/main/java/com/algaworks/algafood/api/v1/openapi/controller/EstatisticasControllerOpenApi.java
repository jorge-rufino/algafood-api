package com.algaworks.algafood.api.v1.openapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.controller.EstatisticasController.EstatisticasModel;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface EstatisticasControllerOpenApi {

	EstatisticasModel estatisticas();

	//	Caso nao seja passado nenhum offset, o valor default utilizado é do UTC (+00:00)
	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffSet);

	ResponseEntity<byte[]> consultarVendasDiariasPdf(VendaDiariaFilter filtro, String timeOffSet);

	Map<String, Object> consultarVendasDiariasComTotalGeral(VendaDiariaFilter filtro, String timeOffSet);

}