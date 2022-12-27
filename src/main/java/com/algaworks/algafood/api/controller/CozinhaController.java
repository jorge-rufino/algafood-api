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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.services.CozinhaService;

//Esta annotation é mais completa e implementa as annotations "@Controller" e "@ResponseBody"
@RestController		
@RequestMapping(value = "/cozinhas") 
public class CozinhaController {
			
	@Autowired
	private CozinhaService cozinhaService;
	
	@GetMapping
	public List<Cozinha> listar(){
		return cozinhaService.listar();
	}
	
	@GetMapping(value = "/{cozinhaId}")
	public ResponseEntity<Cozinha> buscarId(@PathVariable("cozinhaId") Long cozinhaId) {
		Cozinha cozinha = cozinhaService.buscarPorId(cozinhaId);
		System.out.println(cozinha);
		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}

		return ResponseEntity.notFound().build();

	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)		
	public Cozinha adicionar (@RequestBody Cozinha cozinha) {
		return cozinhaService.salvar(cozinha);
	}	
	
	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId,@RequestBody Cozinha cozinha){
		Cozinha cozinhaAtual = cozinhaService.buscarPorId(cozinhaId);
		
		if (cozinhaAtual != null) {

			BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
			
			return ResponseEntity.ok(cozinhaService.salvar(cozinhaAtual));
		}
		
		return ResponseEntity.notFound().build();		
	}
	
	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<?> deletar(@PathVariable Long cozinhaId){

		try {							
			cozinhaService.deletar(cozinhaId);
			
			return ResponseEntity.noContent().build();		
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
