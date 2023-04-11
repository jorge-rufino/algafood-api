package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.model.PermissaoDto;
import com.algaworks.algafood.domain.model.Permissao;

@Component
public class PermissaoDtoAssembler implements RepresentationModelAssembler<Permissao, PermissaoDto>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Override
	public PermissaoDto toModel(Permissao permissao) {
		return modelMapper.map(permissao, PermissaoDto.class);
	}
	
	@Override
    public CollectionModel<PermissaoDto> toCollectionModel(Iterable<? extends Permissao> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities).add(algaLinks.linkToPermissoes());
    }   
}
