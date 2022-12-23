package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
}
