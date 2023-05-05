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

import com.algaworks.algafood.api.v1.assembler.GrupoDtoAssembler;
import com.algaworks.algafood.api.v1.disassembler.GrupoInputDtoDisassembler;
import com.algaworks.algafood.api.v1.model.GrupoDto;
import com.algaworks.algafood.api.v1.model.input.GrupoInputDto;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.services.GrupoService;

@RestController
@RequestMapping(path = "/v1/grupos")
public class GrupoController {
	
	@Autowired
	private GrupoService service;
	
	@Autowired
	private GrupoDtoAssembler grupoDtoAssembler;
	
	@Autowired
	private GrupoInputDtoDisassembler grupoInputDtoDisassembler;
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping
	public CollectionModel<GrupoDto> listar(){
		return grupoDtoAssembler.toCollectionModel(service.listar());
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping("/{id}")
	public GrupoDto buscarPorId(@PathVariable Long id) {
		return grupoDtoAssembler.toModel(service.buscarPorId(id));
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoDto adicionar(@Valid @RequestBody GrupoInputDto grupoInput) {
		Grupo grupo = grupoInputDtoDisassembler.toDomainObject(grupoInput);
		return grupoDtoAssembler.toModel(service.adicionar(grupo));
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@PutMapping("/{id}")
	public GrupoDto atualizar(@PathVariable Long id, @Valid @RequestBody GrupoInputDto grupoInput) {
		Grupo grupoAtual = service.buscarPorId(id);
		grupoInputDtoDisassembler.copyToDomainObject(grupoInput, grupoAtual);
		
		return grupoDtoAssembler.toModel(service.adicionar(grupoAtual));
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		service.deletar(id);
	}
}
