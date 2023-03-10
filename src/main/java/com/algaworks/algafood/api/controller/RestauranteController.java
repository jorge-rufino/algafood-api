package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteDtoAssembler;
import com.algaworks.algafood.api.disassembler.RestauranteInputDtoDisassembler;
import com.algaworks.algafood.api.model.RestauranteDto;
import com.algaworks.algafood.api.model.input.RestauranteInputDto;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.services.RestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteService service;
	
	@Autowired
	private RestauranteDtoAssembler restauranteDtoAssembler;
	
	@Autowired
	private RestauranteInputDtoDisassembler restauranteInputDisassembler;
	
	@GetMapping
	public List<RestauranteDto> listar(){		
		return restauranteDtoAssembler.toCollectionDTO(service.listar());
	}

	@GetMapping(value = "/{id}")
	public RestauranteDto buscarId(@PathVariable Long id){
		Restaurante restaurante = service.buscarPorId(id);
		
		return restauranteDtoAssembler.toDTO(restaurante);
	}

	@PostMapping	
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDto adicionar(@RequestBody @Valid RestauranteInputDto restauranteInput){		
		try {
			Restaurante restaurante =restauranteInputDisassembler.toDomainObject(restauranteInput);
			
			return restauranteDtoAssembler.toDTO(service.salvar(restaurante));
			
//		Podemos utilizar o operador "|" em vez de fazer um outro bloco "try/catch" pois ambas disparam a mesma exception
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}		
	}
		
	@PutMapping("/{id}")
	public RestauranteDto atualizar(@PathVariable Long id,@RequestBody @Valid RestauranteInputDto restauranteInput){
	
//		Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
		
		Restaurante restauranteAtual = service.buscarPorId(id);		
			
		restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
		
	//Não precisamos ignorar a "dataAtualizacao" pois o hibernate se encarrega disso
//		BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro"
//				,"produtos");
		try {
			return restauranteDtoAssembler.toDTO(service.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		} catch (CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id){
		service.deletar(id);
	}
	
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		service.ativar(restauranteId);
	}
	
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		service.inativar(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fecharRestaurante(@PathVariable Long restauranteId) {
		service.fecharRestaurante(restauranteId);
	}
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrirRestaurante(@PathVariable Long restauranteId) {
		service.abrirRestaurante(restauranteId);
	}
}
