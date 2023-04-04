package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLink;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.model.RestauranteBasicoDto;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteBasicoDtoAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteBasicoDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLink algaLinks;
	
	public RestauranteBasicoDtoAssembler() {
		super(RestauranteController.class, RestauranteBasicoDto.class);
	}
	
	@Override
	public RestauranteBasicoDto toModel(Restaurante restaurante) {
		RestauranteBasicoDto restauranteDto = createModelWithId(restaurante.getId(), restaurante);
		modelMapper.map(restaurante, restauranteDto);
		
		restauranteDto.getCozinha().add(algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
		restauranteDto.add(algaLinks.linkToRestaurantes("restaurantes"));
		
		return restauranteDto;
	}

	@Override
	public CollectionModel<RestauranteBasicoDto> toCollectionModel(Iterable<? extends Restaurante> entities) {	
		return super.toCollectionModel(entities).add(algaLinks.linkToRestaurantes());
	}

}
