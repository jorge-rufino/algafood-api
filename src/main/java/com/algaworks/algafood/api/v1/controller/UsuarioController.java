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

import com.algaworks.algafood.api.v1.assembler.UsuarioDtoAssembler;
import com.algaworks.algafood.api.v1.disassembler.UsuarioInputDtoDisassembler;
import com.algaworks.algafood.api.v1.model.UsuarioDto;
import com.algaworks.algafood.api.v1.model.input.SenhaInputDto;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInputDto;
import com.algaworks.algafood.api.v1.model.input.UsuarioInputDto;
import com.algaworks.algafood.api.v1.openapi.controller.UsuarioControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.services.UsuarioService;

@RestController
@RequestMapping(path = "/v1/usuarios")
public class UsuarioController implements UsuarioControllerOpenApi {

	@Autowired
	private UsuarioService service;
	
	@Autowired
	private UsuarioDtoAssembler usuarioDtoAssembler;
	
	@Autowired
	private UsuarioInputDtoDisassembler usuarioInputDtoDisassembler;
	
	@Override
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping
	public CollectionModel<UsuarioDto> listar(){
		return usuarioDtoAssembler.toCollectionModel(service.listar());
	}
	
	@Override
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultarUsuario
	@GetMapping("{usuarioId}")
	public UsuarioDto buscarPorId(@PathVariable Long usuarioId) {
		return usuarioDtoAssembler.toModel(service.buscarPorId(usuarioId));
	}
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioDto adicionar(@RequestBody @Valid UsuarioComSenhaInputDto usuarioInputComSenha) {
		
		Usuario usuario = usuarioInputDtoDisassembler.toDomainObject(usuarioInputComSenha);
		return usuarioDtoAssembler.toModel(service.salvar(usuario));
	}
	
	@Override
	@CheckSecurity.UsuariosGruposPermissoes.PodeAlterarUsuario
	@PutMapping("/{usuarioId}")
	public UsuarioDto atualizar(@PathVariable Long usuarioId, @RequestBody @Valid UsuarioInputDto usuarioInput) {
		Usuario usuarioAtual = service.buscarPorId(usuarioId);
		usuarioInputDtoDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
		
		return usuarioDtoAssembler.toModel(service.salvar(usuarioAtual));	
	}
	
	@Override
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{usuarioId}")
	public void deletar(@PathVariable Long usuarioId) {
		service.deletar(usuarioId);
	}
	
	@Override
	@CheckSecurity.UsuariosGruposPermissoes.PodeAlterarPropriaSenha
	@PutMapping("/{usuarioId}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInputDto senhaInput) {
		service.alterarSenha(usuarioId, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
	}
}
