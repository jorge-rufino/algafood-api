package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.services.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	CidadeService cidadeService;
	
	@GetMapping
	public List<Cidade> listar(){
		return cidadeService.listar();				
	}
	
	@GetMapping("{cidadeId}")
	public ResponseEntity<Cidade> buscarPorId(@PathVariable Long cidadeId){
		
		Cidade cidade = cidadeService.buscarPorId(cidadeId);
		
		if (cidade == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cidade);
	}
	
	@PostMapping	
	public ResponseEntity<?> adicionar (@RequestBody Cidade cidade){
		try {
			return ResponseEntity.ok(cidadeService.salvar(cidade));
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cidade cidade){
		try {
			Cidade cidadeAtual = cidadeService.buscarPorId(id);
			
			if (cidadeAtual != null) {
				BeanUtils.copyProperties(cidade, cidadeAtual, "id");
				cidadeAtual = cidadeService.salvar(cidadeAtual);
				return ResponseEntity.ok(cidadeAtual);
			}
			
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}		
	}
	
	@DeleteMapping("{cidadeId}")
	public ResponseEntity<Cidade> deletar(@PathVariable Long cidadeId){
		try {
			cidadeService.deletar(cidadeId);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
		
		
		return ResponseEntity.noContent().build();
	}
}
