package com.algaworks.algafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.controller.UsuarioGrupoController;
import com.algaworks.algafood.api.model.UsuarioDto;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioDtoAssembler extends RepresentationModelAssemblerSupport<Usuario, UsuarioDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	public UsuarioDtoAssembler() {
		super(UsuarioController.class, UsuarioDto.class);
	}

	@Override
	public UsuarioDto toModel(Usuario usuario) {
		
		UsuarioDto usuarioDto = createModelWithId(usuario.getId(), usuario);		
        modelMapper.map(usuario, usuarioDto);
        
//      Utilizando os passos acima, não é necessário definir o "Self" como abaixo, o spring vai inseri-lo automaticamente.
//		usuarioDto.add(WebMvcLinkBuilder.linkTo(methodOn(UsuarioController.class).buscarPorId(usuarioDto.getId())).withSelfRel());
		
		usuarioDto.add(WebMvcLinkBuilder.linkTo(UsuarioController.class).withRel("usuarios"));
		
		usuarioDto.add(WebMvcLinkBuilder.linkTo(methodOn(UsuarioGrupoController.class).listar(usuarioDto.getId())).withRel("grupos-usuario"));
		
		return usuarioDto;
	}
	
	@Override
	public CollectionModel<UsuarioDto> toCollectionModel(Iterable<? extends Usuario> entities) {	
		return super.toCollectionModel(entities).add(WebMvcLinkBuilder.linkTo(UsuarioController.class).withSelfRel());
	}
}
