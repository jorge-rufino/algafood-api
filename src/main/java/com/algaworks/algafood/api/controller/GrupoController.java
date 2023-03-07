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

import com.algaworks.algafood.api.assembler.GrupoDtoAssembler;
import com.algaworks.algafood.api.disassembler.GrupoInputDtoDisassembler;
import com.algaworks.algafood.api.model.GrupoDto;
import com.algaworks.algafood.api.model.input.GrupoInputDto;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.services.GrupoService;

@RestController
@RequestMapping("/grupos")
public class GrupoController {
	
	@Autowired
	private GrupoService service;
	
	@Autowired
	private GrupoDtoAssembler grupoDtoAssembler;
	
	@Autowired
	private GrupoInputDtoDisassembler grupoInputDtoDisassembler;
	
	@GetMapping
	public List<GrupoDto> listar(){
		return grupoDtoAssembler.toCollectionDto(service.listar());
	}
	
	@GetMapping("/{id}")
	public GrupoDto buscarPorId(@PathVariable Long id) {
		return grupoDtoAssembler.toDto(service.buscarPorId(id));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoDto adicionar(@Valid @RequestBody GrupoInputDto grupoInput) {
		Grupo grupo = grupoInputDtoDisassembler.toDomainObject(grupoInput);
		return grupoDtoAssembler.toDto(service.adicionar(grupo));
	}
	
	@PutMapping("/{id}")
	public GrupoDto atualizar(@PathVariable Long id, @Valid @RequestBody GrupoInputDto grupoInput) {
		Grupo grupoAtual = service.buscarPorId(id);
		grupoInputDtoDisassembler.copyToDomainObject(grupoInput, grupoAtual);
		
		return grupoDtoAssembler.toDto(service.adicionar(grupoAtual));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		service.deletar(id);
	}
}
