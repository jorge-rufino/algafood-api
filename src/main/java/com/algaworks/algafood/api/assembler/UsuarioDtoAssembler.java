package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLink;
import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.model.UsuarioDto;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioDtoAssembler extends RepresentationModelAssemblerSupport<Usuario, UsuarioDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLink algaLinks;
	public UsuarioDtoAssembler() {
		super(UsuarioController.class, UsuarioDto.class);
	}

	@Override
	public UsuarioDto toModel(Usuario usuario) {
		
		UsuarioDto usuarioDto = createModelWithId(usuario.getId(), usuario);		
        modelMapper.map(usuario, usuarioDto);
        
        usuarioDto.add(algaLinks.linkToUsuarios("usuarios"));
        usuarioDto.add(algaLinks.linkToGruposUsuario(usuario.getId(), "grupos-usuario"));
		
		return usuarioDto;
	}
	
	@Override
	public CollectionModel<UsuarioDto> toCollectionModel(Iterable<? extends Usuario> entities) {	
		return super.toCollectionModel(entities).add(algaLinks.linkToUsuarios());
	}
}
