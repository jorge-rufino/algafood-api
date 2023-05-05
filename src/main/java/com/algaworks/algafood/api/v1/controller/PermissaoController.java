package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.PermissaoDtoAssembler;
import com.algaworks.algafood.api.v1.model.PermissaoDto;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.services.PermissaoService;

@RestController
@RequestMapping(path = "/v1/permissoes")
public class PermissaoController {

	@Autowired
	private PermissaoService permissaoService;
	
	@Autowired
	private PermissaoDtoAssembler permissaoDtoAssembler;
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping
	public CollectionModel<PermissaoDto> lista(){
		return permissaoDtoAssembler.toCollectionModel(permissaoService.listar());
	}
}
