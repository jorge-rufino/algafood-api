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

import com.algaworks.algafood.api.assembler.EstadoDtoAssembler;
import com.algaworks.algafood.api.disassembler.EstadoInputDtoDisassembler;
import com.algaworks.algafood.api.model.EstadoDto;
import com.algaworks.algafood.api.model.input.EstadoInputDto;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.services.EstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {
	
	@Autowired
	private EstadoService service;
	
	@Autowired
	private EstadoDtoAssembler estadoDtoAssembler;
	
	@Autowired
	private EstadoInputDtoDisassembler estadoInputDtoDisassembler;
	
	@GetMapping
	public List<EstadoDto> listar(){
		return estadoDtoAssembler.toCollectionDto(service.listar());
	}
	
	@GetMapping(value = "/{estadoId}")
	public EstadoDto buscarId (@PathVariable Long estadoId){
		return estadoDtoAssembler.toDto(service.buscarPorId(estadoId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoDto adicionar (@RequestBody @Valid EstadoInputDto estadoInput){
		Estado estado = estadoInputDtoDisassembler.toDomainObject(estadoInput); 
		return estadoDtoAssembler.toDto(service.salvar(estado));	
	}
	
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar (@PathVariable Long estadoId) {		
		service.deletar(estadoId);
	}
	
	@PutMapping("{estadoId}")
	public EstadoDto atualizar (@RequestBody @Valid EstadoInputDto estadoInput, @PathVariable Long estadoId){		
		Estado estadoAtual = service.buscarPorId(estadoId);		
		estadoInputDtoDisassembler.copyToDomainObject(estadoInput, estadoAtual);
				
		return estadoDtoAssembler.toDto(service.salvar(estadoAtual));
	}
}
