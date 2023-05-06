package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoDto;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteBasicoDtoAssembler
		extends RepresentationModelAssemblerSupport<Restaurante, RestauranteBasicoDto> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AlgaLinks algaLinks;

	@Autowired
	private AlgaSecurity algaSecurity;

	public RestauranteBasicoDtoAssembler() {
		super(RestauranteController.class, RestauranteBasicoDto.class);
	}

	@Override
	public RestauranteBasicoDto toModel(Restaurante restaurante) {
		RestauranteBasicoDto restauranteDto = createModelWithId(restaurante.getId(), restaurante);
		modelMapper.map(restaurante, restauranteDto);

		if (algaSecurity.podeConsultarRestaurantes()) {
			restauranteDto.add(algaLinks.linkToRestaurantes("restaurantes"));
		}

		if (algaSecurity.podeConsultarCozinhas()) {
			restauranteDto.getCozinha().add(algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
		}

		return restauranteDto;
	}

	@Override
	public CollectionModel<RestauranteBasicoDto> toCollectionModel(Iterable<? extends Restaurante> entities) {
		CollectionModel<RestauranteBasicoDto> collectionModel = super.toCollectionModel(entities);

		if (algaSecurity.podeConsultarRestaurantes()) {
			collectionModel.add(algaLinks.linkToRestaurantes());
		}

		return collectionModel;
	}

}
