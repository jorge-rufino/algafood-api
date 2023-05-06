package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteApenasNomeDto;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteApenasNomeDtoAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteApenasNomeDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity;  
	
	public RestauranteApenasNomeDtoAssembler() {
		super(RestauranteController.class, RestauranteApenasNomeDto.class);
	}
	
	@Override
	public RestauranteApenasNomeDto toModel(Restaurante restaurante) {
		RestauranteApenasNomeDto restauranteDto = createModelWithId(restaurante.getId(), restaurante);
		modelMapper.map(restaurante, restauranteDto);
		
		if (algaSecurity.podeConsultarRestaurantes()) {
			restauranteDto.add(algaLinks.linkToRestaurantes("restaurantes"));
		}
		
		return restauranteDto;
	}

	@Override
	public CollectionModel<RestauranteApenasNomeDto> toCollectionModel(Iterable<? extends Restaurante> entities) {	
		CollectionModel<RestauranteApenasNomeDto> collectionModel = super.toCollectionModel(entities);
	    
	    if (algaSecurity.podeConsultarRestaurantes()) {
	        collectionModel.add(algaLinks.linkToRestaurantes());
	    }
	    
	    return collectionModel;
	}

}
