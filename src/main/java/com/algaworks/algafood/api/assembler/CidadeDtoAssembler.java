package com.algaworks.algafood.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.CidadeDto;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeDtoAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CidadeDto toDto(Cidade cidade) {
		return modelMapper.map(cidade, CidadeDto.class);
	}
	
	public List<CidadeDto> toCollectionDto(List<Cidade> cidades){
		return cidades.stream()
				.map(cidade -> toDto(cidade))
				.toList();
	}
}
