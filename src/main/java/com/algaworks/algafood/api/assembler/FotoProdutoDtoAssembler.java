package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.FotoProdutoDto;
import com.algaworks.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoDtoAssembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public FotoProdutoDto toDto(FotoProduto foto) {
		return modelMapper.map(foto, FotoProdutoDto.class);
	}
	
}
