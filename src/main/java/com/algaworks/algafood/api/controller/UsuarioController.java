package com.algaworks.algafood.api.controller;

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

import com.algaworks.algafood.api.assembler.UsuarioDtoAssembler;
import com.algaworks.algafood.api.disassembler.UsuarioInputDtoDisassembler;
import com.algaworks.algafood.api.model.UsuarioDto;
import com.algaworks.algafood.api.model.input.SenhaInputDto;
import com.algaworks.algafood.api.model.input.UsuarioComSenhaInputDto;
import com.algaworks.algafood.api.model.input.UsuarioInputDto;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@Autowired
	private UsuarioDtoAssembler usuarioDtoAssembler;
	
	@Autowired
	private UsuarioInputDtoDisassembler usuarioInputDtoDisassembler;
	
	@GetMapping
	public CollectionModel<UsuarioDto> listar(){
		return usuarioDtoAssembler.toCollectionModel(service.listar());
	}
	
	@GetMapping("{id}")
	public UsuarioDto buscarPorId(@PathVariable Long id) {
		return usuarioDtoAssembler.toModel(service.buscarPorId(id));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioDto adicionar(@RequestBody @Valid UsuarioComSenhaInputDto usuarioInputComSenha) {
		
		Usuario usuario = usuarioInputDtoDisassembler.toDomainObject(usuarioInputComSenha);
		return usuarioDtoAssembler.toModel(service.salvar(usuario));
	}
	
	@PutMapping("/{id}")
	public UsuarioDto atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioInputDto usuarioInput) {
		Usuario usuarioAtual = service.buscarPorId(id);
		usuarioInputDtoDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
		
		return usuarioDtoAssembler.toModel(service.salvar(usuarioAtual));	
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		service.deletar(id);
	}
	
	@PutMapping("/{id}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void alterarSenha(@PathVariable Long id, @RequestBody @Valid SenhaInputDto senhaInput) {
		service.alterarSenha(id, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
	}
}
