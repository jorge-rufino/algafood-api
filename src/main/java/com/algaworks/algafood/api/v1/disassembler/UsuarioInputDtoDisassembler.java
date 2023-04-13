package com.algaworks.algafood.api.v1.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.model.input.UsuarioInputDto;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioInputDtoDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Usuario toDomainObject(UsuarioInputDto usuarioInput) {
		return modelMapper.map(usuarioInput, Usuario.class);
	}
	
	public void copyToDomainObject(UsuarioInputDto usuarioInput, Usuario usuario) {
		modelMapper.map(usuarioInput, usuario);
	}
}
