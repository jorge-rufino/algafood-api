package com.algaworks.algafood.api.v1.controller;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.services.VendaQueryService;
import com.algaworks.algafood.domain.services.VendaReportService;

@RestController
@RequestMapping(path = "/estatisticas")
public class EstatisticasController {

	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Autowired
	private VendaReportService vendaReportService;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public EstatisticasModel estatisticas() {
		var estatisticasModel = new EstatisticasModel();
		
		estatisticasModel.add(algaLinks.linkToEstatisticasVendasDiarias("vendas-diarias"));
		
		return estatisticasModel;
	}
	
//	Caso nao seja passado nenhum offset, o valor default utilizado é do UTC (+00:00)
	@GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VendaDiaria> consultarVendasDiarias(
			VendaDiariaFilter filtro,@RequestParam(required = false, defaultValue = "+00:00")  String timeOffSet){
		return vendaQueryService.consultarVendasDiarias(filtro,timeOffSet);
	}
	
	@GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> consultarVendasDiariasPdf(
			VendaDiariaFilter filtro,@RequestParam(required = false, defaultValue = "+00:00")  String timeOffSet){
		
		byte[] bytesPdf = vendaReportService.emitirVendasDiarias(filtro, timeOffSet);
		
		var headers = new HttpHeaders();
		
//		attachment => Faz com que o relatorio seja baixado(feito o download) pelo cliente e não somente aberto in-line
//		filename => nome sugerido no momento de salvar o arquivo
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");
		
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.headers(headers)
				.body(bytesPdf);
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
	
	public static class EstatisticasModel extends RepresentationModel<EstatisticasModel> {
	}
}
