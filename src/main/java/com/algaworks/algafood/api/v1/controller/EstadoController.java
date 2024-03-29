package com.algaworks.algafood.api.v1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

import com.algaworks.algafood.api.v1.assembler.EstadoDtoAssembler;
import com.algaworks.algafood.api.v1.disassembler.EstadoInputDtoDisassembler;
import com.algaworks.algafood.api.v1.model.EstadoDto;
import com.algaworks.algafood.api.v1.model.input.EstadoInputDto;
import com.algaworks.algafood.api.v1.openapi.controller.EstadoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.services.EstadoService;

@RestController
@RequestMapping(path = "/v1/estados")
public class EstadoController implements EstadoControllerOpenApi {
	
	@Autowired
	private EstadoService service;
	
	@Autowired
	private EstadoDtoAssembler estadoDtoAssembler;
	
	@Autowired
	private EstadoInputDtoDisassembler estadoInputDtoDisassembler;
	
	@Override
	@CheckSecurity.Estados.PodeConsultar
	@GetMapping
	public CollectionModel<EstadoDto> listar(){
		return estadoDtoAssembler.toCollectionModel(service.listar());
	}
	
	@Override
	@CheckSecurity.Estados.PodeConsultar
	@GetMapping(value = "/{estadoId}")
	public EstadoDto buscarId (@PathVariable Long estadoId){
		return estadoDtoAssembler.toModel(service.buscarPorId(estadoId));
	}
	
	@Override
	@CheckSecurity.Estados.PodeEditar
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoDto adicionar (@RequestBody @Valid EstadoInputDto estadoInput){
		Estado estado = estadoInputDtoDisassembler.toDomainObject(estadoInput); 
		return estadoDtoAssembler.toModel(service.salvar(estado));	
	}
	
	@Override
	@CheckSecurity.Estados.PodeEditar
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar (@PathVariable Long estadoId) {		
		service.deletar(estadoId);
	}
	
	@Override
	@CheckSecurity.Estados.PodeEditar
	@PutMapping("{estadoId}")
	public EstadoDto atualizar (@RequestBody @Valid EstadoInputDto estadoInput, @PathVariable Long estadoId){		
		Estado estadoAtual = service.buscarPorId(estadoId);		
		estadoInputDtoDisassembler.copyToDomainObject(estadoInput, estadoAtual);
				
		return estadoDtoAssembler.toModel(service.salvar(estadoAtual));
	}
}
