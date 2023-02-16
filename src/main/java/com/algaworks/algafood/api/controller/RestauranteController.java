package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.services.RestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	RestauranteService service;
	
	@GetMapping
	public List<Restaurante> listar(){
		return service.listar();
	}
	
	@GetMapping(value = "/{id}")
	public Restaurante buscarId(@PathVariable Long id){		
		return service.buscarPorId(id);
	}

	@PostMapping	
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurante adicionar(@RequestBody @Valid Restaurante restaurante){		
		try {
			return service.salvar(restaurante);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}		
	}
		
	@PutMapping("/{id}")
	public Restaurante atualizar(@PathVariable Long id,@RequestBody Restaurante restaurante){
	
		Restaurante restauranteAtual = service.buscarPorId(id);
			
	//Não precisamos ignorar a "dataAtualizacao" pois o hibernate se encarrega disso
		BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro"
				,"produtos");
		
		try {
			return service.salvar(restauranteAtual);
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id){
		service.deletar(id);
	}
	
	@PatchMapping("/{id}")
	public Restaurante atualizarParcial (@PathVariable Long id, @RequestBody Map<String, Object> campos, HttpServletRequest request){
		
		Restaurante restauranteAtual = service.buscarPorId(id);
		
		merge(campos, restauranteAtual, request);
		
		return atualizar(id, restauranteAtual);
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		
//		ServletServerHttpRequest implementa ServerHttpRequest que implementa o HttpInputMessage que precisamos passar como 
//		paramentro no catch
		
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			
//			O "ObjectMapper" não é gerenciado pelo spring, portanto precisamos configurar para disparar as exceções
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
//			Converte os campos para os mesmos da classe Restaurante para evitar erros de conversão de dados
			Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
			
			camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				
//				Aqui, o Refletcion busca nas proriedades da classe (Restaurante) algum campo que tenha o nome de "nomePropriedade"
				Field field  = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				
//				Torna os campos da classe, no caso Restaurante, que são privados (private) acessíveis
				field.setAccessible(true);
				
				Object valorPropriedadeConvertido = ReflectionUtils.getField(field, restauranteOrigem);
				
				ReflectionUtils.setField(field, restauranteDestino, valorPropriedadeConvertido);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			//Precisamos usar esse construtor pois ele não está "depreciado"
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
		
	}
}
