package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.PermissaoDtoAssembler;
import com.algaworks.algafood.api.v1.model.PermissaoDto;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.services.GrupoService;

@RestController
@RequestMapping(path = "/v1/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

	@Autowired
	private GrupoService grupoService;
	
	@Autowired
	private PermissaoDtoAssembler permissaoDtoAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity;  
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping
	public CollectionModel<PermissaoDto> listar(@PathVariable Long grupoId){
		Grupo grupo = grupoService.buscarPorId(grupoId);
		
		 CollectionModel<PermissaoDto> permissoesModel = 
				 permissaoDtoAssembler.toCollectionModel(grupo.getPermissoes())
	            .removeLinks()
	            .add(algaLinks.linkToGrupoPermissoes(grupoId));
		 
		 if (algaSecurity.podeEditarUsuariosGruposPermissoes()) {
			 permissoesModel.add(algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));
			 
			 permissoesModel.getContent().forEach(permissaoModel -> {
				 permissaoModel.add(algaLinks.linkToGrupoPermissaoDesassociacao(
						 grupoId, permissaoModel.getId(), "desassociar"));
			 });
		 }
	    
	    return permissoesModel;
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		grupoService.desassociarPermissao(grupoId, permissaoId);
		
		return ResponseEntity.noContent().build();
	}
	
	@CheckSecurity.UsuariosGruposPermissoes.PodeEditar
	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		grupoService.associarPermissao(grupoId, permissaoId);
		
		return ResponseEntity.noContent().build();
	}
	
}
