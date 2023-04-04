package com.algaworks.algafood.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.FormaPagamentoDtoAssembler;
import com.algaworks.algafood.api.model.FormaPagamentoDto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.services.RestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private FormaPagamentoDtoAssembler formaPagamentoAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@GetMapping
	public CollectionModel<FormaPagamentoDto> listar(@PathVariable Long restauranteId){
		Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
		
		return formaPagamentoAssembler.toCollectionModel(restaurante.getFormasPagamento())
				.removeLinks()
				.add(algaLinks.linkToRestauranteFormasPagamento(restaurante.getId()));
	}	
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociarFormaPagamento (@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		restauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);
	}
	
	@PutMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associarFormaPagamento (@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		restauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
	}
}
