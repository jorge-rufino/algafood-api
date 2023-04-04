package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLink;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.model.RestauranteDto;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteDtoAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLink algaLinks;
	
	public RestauranteDtoAssembler() {
		super(RestauranteController.class, RestauranteDto.class);
	}
	
	@Override
	public RestauranteDto toModel(Restaurante restaurante) {
		RestauranteDto restauranteDto = createModelWithId(restaurante.getId(), restaurante);
		modelMapper.map(restaurante, restauranteDto);
		
		restauranteDto.getCozinha().add(algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
		restauranteDto.getEndereco().getCidade().add(algaLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
		restauranteDto.add(algaLinks.linkToRestaurantes("restaurantes"));
		restauranteDto.add(algaLinks.linkToRestauranteFormasPagamento(restaurante.getId(), "formas-pagamento"));
		restauranteDto.add(algaLinks.linkToResponsaveisRestaurante(restaurante.getId(), "responsaveis"));
		
		return restauranteDto;
	}

	@Override
	public CollectionModel<RestauranteDto> toCollectionModel(Iterable<? extends Restaurante> entities) {	
		return super.toCollectionModel(entities).add(algaLinks.linkToRestaurantes().withSelfRel());
	}
	
//	public List<RestauranteDto> toCollectionDTO(List<Restaurante> restaurantes){
//		return restaurantes.stream()
//			.map(restaurante -> toModel(restaurante))	
//			.sorted(Comparator.comparing(RestauranteDto::getId))	//Tive que adicinar para organizar a lista por ID
//			.collect(Collectors.toList());
//	}

}
