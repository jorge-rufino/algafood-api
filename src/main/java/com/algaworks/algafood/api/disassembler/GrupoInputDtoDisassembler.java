package com.algaworks.algafood.api.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.GrupoInputDto;
import com.algaworks.algafood.domain.model.Grupo;

@Component
public class GrupoInputDtoDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Grupo toDomainObject(GrupoInputDto grupoInput) {
		return modelMapper.map(grupoInput, Grupo.class);
	}
	
	public void copyToDomainObject(GrupoInputDto grupoInput, Grupo grupo) {
		modelMapper.map(grupoInput, grupo);
	}
}
