package com.algaworks.algafood.api.controller;

import java.util.List;

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

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.services.EstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {
	
	@Autowired
	private EstadoService service;
	
	@GetMapping
	public List<Estado> listar(){
		return service.listar();
	}
	
	@GetMapping(value = "/{estadoId}")
	public Estado buscarId (@PathVariable Long estadoId){
		return service.buscarPorId(estadoId);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Estado adicionar (@RequestBody Estado estado){
		return service.salvar(estado);	
	}
	
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar (@PathVariable Long estadoId) {		
		service.deletar(estadoId);
	}
	
	@PutMapping("{estadoId}")
	public Estado atualizar (@RequestBody Estado estado, @PathVariable Long estadoId){
		
		Estado estadoAtual = service.buscarPorId(estadoId);		
		
		BeanUtils.copyProperties(estado, estadoAtual, "id");
		
		return service.salvar(estadoAtual);
	}
}
