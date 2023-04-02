package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLink;
import com.algaworks.algafood.api.controller.CozinhaController;
import com.algaworks.algafood.api.model.CozinhaDto;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaDtoAssembler extends RepresentationModelAssemblerSupport<Cozinha, CozinhaDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLink algaLinks;
	
	public CozinhaDtoAssembler() {
		super(CozinhaController.class, CozinhaDto.class);
	}
	
	@Override
	public CozinhaDto toModel(Cozinha cozinha) {
		CozinhaDto cozinhaDto = createModelWithId(cozinha.getId(), cozinha);
		modelMapper.map(cozinha, cozinhaDto);
		
		cozinhaDto.add(algaLinks.linkToCozinhas("cozinhas"));
		
		return cozinhaDto;
	}
	
}
