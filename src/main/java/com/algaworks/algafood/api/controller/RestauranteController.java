package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.services.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	RestauranteRepository repository;
	
	@Autowired
	RestauranteService service;
	
	@GetMapping
	public List<Restaurante> listar(){
		return repository.listar();
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Restaurante> buscarId(@PathVariable Long id){
		Restaurante restaurante = repository.buscarPorId(id);
		
		if (restaurante != null) {
			
			return ResponseEntity.ok(restaurante);
		}
		
		return ResponseEntity.notFound().build();
	}
//	O "?" permite retornar qualquer coisa. Utilizamos para poder retornar uma mensagem de erro ou um "Restaurante"
	@PostMapping	
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante){
		try {
			restaurante = service.salvar(restaurante);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
					
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id,@RequestBody Restaurante restaurante){
		try {
			Restaurante restauranteAtual = service.buscarPorId(id);
			
//			Não fiz essa validação para disparar uma Exception na classe RestauranteService pois como só temos uma exceção por enquanto
//			ela seria disparada tanto quando não houve nem cozinha nem restaurante, então não teria como distinguir
			if(restauranteAtual != null) {
				BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
				
				restauranteAtual = service.salvar(restauranteAtual);
				
				return ResponseEntity.ok(restauranteAtual);
			}
			
			return ResponseEntity.notFound().build(); 
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Restaurante> deletar(@PathVariable Long id){
		try {
			service.deletar(id);
			
			return ResponseEntity.noContent().build();		
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> atualizarParcial (@PathVariable Long id, @RequestBody Map<String, Object> campos){
		
		Restaurante restauranteAtual = service.buscarPorId(id);
		
		if(restauranteAtual == null) {
			return ResponseEntity.notFound().build();
		}
		
		merge(campos, restauranteAtual);
		
		return atualizar(id, restauranteAtual);
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
//		Converte os campos para os mesmos da classe Restaurante para evitar erros de conversão de dados
		Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);
		
		camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			
//			Aqui, o Refletcion busca nas proriedades da classe (Restaurante) algum campo que tenha o nome de "nomePropriedade"
			Field field  = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			
//			Torna os campos da classe, no caso Restaurante, que são privados (private) acessíveis
			field.setAccessible(true);
			
			Object valorPropriedadeConvertido = ReflectionUtils.getField(field, restauranteOrigem);
			
			ReflectionUtils.setField(field, restauranteDestino, valorPropriedadeConvertido);
		});
	}
}
