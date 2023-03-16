package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.services.VendaQueryService;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticasController {

	@Autowired
	private VendaQueryService vendaQueryService;
	
//	Caso nao seja passado nenhum offset, o valor default utilizado é do UTC (+00:00)
	@GetMapping("/vendas-diarias")
	public List<VendaDiaria> consultarVendasDiarias(
			VendaDiariaFilter filtro,@RequestParam(required = false, defaultValue = "+00:00")  String timeOffSet){
		return vendaQueryService.consultarVendasDiarias(filtro,timeOffSet);
	}
	
	@GetMapping("/vendas-diarias-com-total-geral")
	public Map<String, Object> consultarVendasDiariasComTotalGeral(VendaDiariaFilter filtro, String timeOffSet){
		List<VendaDiaria> vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro,timeOffSet);
		
		Map<String, Object> respostaJson = new LinkedHashMap<>();
		respostaJson.put("content", vendasDiarias);
		respostaJson.put("Total de dias: ", vendasDiarias.size());
		respostaJson.put("Quantidade total de vendas: ", vendasDiarias.stream()
				.map(venda -> venda.getTotalVendas())
				.reduce((long) 0, (n1, n2) -> (n1 + n2)));
		
		respostaJson.put("Faturamento total: ", vendasDiarias.stream()
				.map(item-> item.getTotalFaturado())
				.reduce(BigDecimal.ZERO,BigDecimal::add));
	
		return respostaJson;
	}
}
