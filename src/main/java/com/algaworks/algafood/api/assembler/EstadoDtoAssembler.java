package com.algaworks.algafood.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.EstadoDto;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoDtoAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public EstadoDto toDto(Estado estado) {
		return modelMapper.map(estado, EstadoDto.class);
	}
	
	public List<EstadoDto> toCollectionDto (List<Estado> estados){
		return estados.stream()
				.map(estado -> toDto(estado))
				.toList();
	}
}
