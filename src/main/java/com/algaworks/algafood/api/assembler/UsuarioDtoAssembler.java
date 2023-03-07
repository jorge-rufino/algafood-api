package com.algaworks.algafood.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.UsuarioDto;
import com.algaworks.algafood.domain.model.Usuario;

@Component
public class UsuarioDtoAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public UsuarioDto toDto(Usuario usuario) {
		return modelMapper.map(usuario, UsuarioDto.class);
	}
	
	public List<UsuarioDto> toCollectDto(List<Usuario> usuarios){
		return usuarios.stream()
				.map(usuario -> toDto(usuario))
				.toList();
	}
}
