package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.FormaPagamentoDtoAssembler;
import com.algaworks.algafood.api.v1.model.FormaPagamentoDto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.services.RestauranteService;

@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private FormaPagamentoDtoAssembler formaPagamentoAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
//	Como não queremos que os Links de associar/desassociar apareçam somente neste recurso e não em outros, criamos o link no controller mesmo
	
	@GetMapping
	public CollectionModel<FormaPagamentoDto> listar(@PathVariable Long restauranteId){
		Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
		
		CollectionModel<FormaPagamentoDto> formasPagamentoDto = formaPagamentoAssembler.toCollectionModel(restaurante.getFormasPagamento())
				.removeLinks()
				.add(algaLinks.linkToRestauranteFormasPagamento(restaurante.getId()))
				.add(algaLinks.linkToRestauranteFormaPagamentoAssociacao(restaurante.getId(), "associar"));
		
		formasPagamentoDto.getContent().forEach(formaPagamento -> {
			formaPagamento.add(
					algaLinks.linkToRestauranteFormaPagamentoDesassociacao(restaurante.getId(), formaPagamento.getId(), "desassociar"));
		});
		
		return formasPagamentoDto;
	}	
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociarFormaPagamento (@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		restauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associarFormaPagamento (@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		restauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
		return ResponseEntity.noContent().build();
	}
}
