package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.model.RestauranteDto;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteDtoAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
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
		
		if(restaurante.isAtivo()) 
			restauranteDto.add(algaLinks.linkToRestauranteInativacao(restaurante.getId(), "inativar"));
		
		if(restaurante.isInativo()) 
			restauranteDto.add(algaLinks.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));

		if(restaurante.isAberto())
			restauranteDto.add(algaLinks.linkToRestauranteFechamento(restaurante.getId(), "fechar"));

		if(restaurante.isFechado() && restaurante.isAtivo())
			restauranteDto.add(algaLinks.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
		
		return restauranteDto;
	}

	@Override
	public CollectionModel<RestauranteDto> toCollectionModel(Iterable<? extends Restaurante> entities) {	
		return super.toCollectionModel(entities).add(algaLinks.linkToRestaurantes().withSelfRel());
	}
	
}
