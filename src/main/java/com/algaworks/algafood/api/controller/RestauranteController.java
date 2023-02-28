package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
import com.algaworks.algafood.api.model.RestauranteDTO;
import com.algaworks.algafood.api.model.input.RestauranteInputDTO;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.services.RestauranteService;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteService service;
	
	@Autowired
	private RestauranteDtoAssembler restauranteDtoAssembler;
	
	@GetMapping
	public List<RestauranteDTO> listar(){		
		return restauranteDtoAssembler.toCollectionDTO(service.listar());
	}

	@GetMapping(value = "/{id}")
	public RestauranteDTO buscarId(@PathVariable Long id){
		Restaurante restaurante = service.buscarPorId(id);
		
		return restauranteDtoAssembler.toDTO(restaurante);
	}

	@PostMapping	
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDTO adicionar(@RequestBody @Valid RestauranteInputDTO restauranteInput){		
		try {
			Restaurante restaurante = toDomainObject(restauranteInput);
			
			return restauranteDtoAssembler.toDTO(service.salvar(restaurante));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}		
	}
		
	@PutMapping("/{id}")
	public RestauranteDTO atualizar(@PathVariable Long id,@RequestBody @Valid RestauranteInputDTO restauranteInput){
	
		Restaurante restaurante = toDomainObject(restauranteInput);
		
		Restaurante restauranteAtual = service.buscarPorId(id);		
			
	//Não precisamos ignorar a "dataAtualizacao" pois o hibernate se encarrega disso
		BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro"
				,"produtos");
		try {
			return restauranteDtoAssembler.toDTO(service.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id){
		service.deletar(id);
	}
	
//	Converte de "RestauranteInputDTO" para "Restaurante"
	private Restaurante toDomainObject(RestauranteInputDTO restauranteInput) {
		Restaurante restaurante = new Restaurante();
		restaurante.setNome(restauranteInput.getNome());
		restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(restauranteInput.getCozinha().getId());
		
		restaurante.setCozinha(cozinha);
		
		return restaurante;
		
	}
}
